import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.zip.*;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class FileCompressionGUI extends JFrame {
    private JButton compressButton;
    private JButton decompressButton;
    private JTextField filePathField;
    private JFileChooser fileChooser;
    private File selectedFile;

    public FileCompressionGUI() {
        setTitle("Compresso");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window

        ImageIcon image = new ImageIcon("Icon.jpg");
        setIconImage(image.getImage());
        
        // Set background color
        getContentPane().setBackground(new Color(240, 248, 255));
        
        filePathField = new JTextField(40);
        filePathField.setEditable(false);

        
        fileChooser = new JFileChooser();
        

        compressButton = new JButton("Compress");
        compressButton.setBackground(new Color(50, 205, 50)); // Green button
        compressButton.setForeground(Color.WHITE);

        decompressButton = new JButton("Decompress");
        decompressButton.setBackground(new Color(30, 144, 255)); // Blue button
        decompressButton.setForeground(Color.WHITE);

        compressButton.addActionListener(e -> {
            if (selectedFile != null) {
                PasswordDialog passwordDialog = new PasswordDialog(this);
                passwordDialog.setVisible(true);
                String password = passwordDialog.getPassword();
                String inputPath = selectedFile.getAbsolutePath();
                String outputPath = inputPath + ".zip";

                if (password != null && !password.isEmpty()) {
                    compressAndEncryptFile(inputPath, outputPath, password);
                } else {
                    compressFile(inputPath, outputPath);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No file selected for compression.");
            }
        });

        decompressButton.addActionListener(e -> {
            if (selectedFile != null) {
                PasswordDialog passwordDialog = new PasswordDialog(this);
                passwordDialog.setVisible(true);
                String password = passwordDialog.getPassword();
                String inputPath = selectedFile.getAbsolutePath();
                String outputPath = inputPath.replace(".zip", ".decompressed");

                if (password != null && !password.isEmpty()) {
                    decryptAndDecompressFile(inputPath, outputPath, password);
                } 
                // else if (password == null) { // Skip button clicked
                //     JOptionPane.showMessageDialog(this, "Decompression requires a password. Operation canceled.");
                // } 
                else {
                    decompressFile(inputPath, outputPath);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No file selected for decompression.");
            }
        });

        JButton chooseFileButton = new JButton("Browse File");
        chooseFileButton.setBackground(new Color(255, 140, 0)); // Orange button
        chooseFileButton.setForeground(Color.WHITE);

        chooseFileButton.addActionListener(e -> {
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(this, "File Selected: " + selectedFile.getName());
            }
        });

  
  // Panel for file path and Browse button (top section)
  JPanel topPanel = new JPanel();
  topPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the file path and browse button
  topPanel.add(filePathField);
  topPanel.add(chooseFileButton);


  // Panel for Compress and Decompress buttons (bottom section)
  JPanel bottomPanel = new JPanel();
  bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the buttons
  bottomPanel.add(compressButton);
  bottomPanel.add(decompressButton);

  // Set layout for the frame
  setLayout(new BorderLayout());
  add(topPanel, BorderLayout.NORTH);  // Add the top panel with file path and browse button
  add(bottomPanel, BorderLayout.CENTER);  // Add the bottom panel with Compress and Decompress buttons

    }

    private void compressFile(String inputPath, String outputPath) {
        try (FileInputStream fis = new FileInputStream(inputPath);
             FileOutputStream fos = new FileOutputStream(outputPath);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            ZipEntry zipEntry = new ZipEntry(new File(inputPath).getName());
            zipOut.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zipOut.write(buffer, 0, length);
            }

            zipOut.closeEntry();
            JOptionPane.showMessageDialog(this, "File Compressed Successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error Compressing File: " + e.getMessage());
        }
    }

    private void compressAndEncryptFile(String inputPath, String outputPath, String password) {
        try (FileInputStream fis = new FileInputStream(inputPath);
             FileOutputStream fos = new FileOutputStream(outputPath);
             CipherOutputStream cos = new CipherOutputStream(fos, initCipher(password, Cipher.ENCRYPT_MODE))) {

            ZipOutputStream zipOut = new ZipOutputStream(cos);
            ZipEntry zipEntry = new ZipEntry(new File(inputPath).getName());
            zipOut.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zipOut.write(buffer, 0, length);
            }

            zipOut.closeEntry();
            zipOut.close();
            JOptionPane.showMessageDialog(this, "File Compressed and Encrypted Successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error Compressing File: " + e.getMessage());
        }
    }

    private void decompressFile(String inputPath, String outputPath) {
        try (FileInputStream fis = new FileInputStream(inputPath);
             ZipInputStream zipIn = new ZipInputStream(fis)) {
    
            ZipEntry entry = zipIn.getNextEntry();
            if (entry != null) {
                String originalFileName = entry.getName(); // Extract original file name
                outputPath = inputPath.substring(0, inputPath.lastIndexOf('.')) + "_" + originalFileName;
    
                try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipIn.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                JOptionPane.showMessageDialog(this, "File Decompressed Successfully: " + outputPath);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error Decompressing File: " + e.getMessage());
        }
    }
        
    private void decryptAndDecompressFile(String inputPath, String outputPath, String password) {
        try (FileInputStream fis = new FileInputStream(inputPath);
             CipherInputStream cis = new CipherInputStream(fis, initCipher(password, Cipher.DECRYPT_MODE));
             ZipInputStream zipIn = new ZipInputStream(cis)) {
    
            ZipEntry entry = zipIn.getNextEntry();
            if (entry != null) {
                String originalFileName = entry.getName();
                outputPath = inputPath.substring(0, inputPath.lastIndexOf('.')) + "_" + originalFileName;
    
                try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipIn.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                JOptionPane.showMessageDialog(this, "File Decrypted and Decompressed Successfully: " + outputPath);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Incorrect password or file corrupted.");
            }
        } catch (javax.crypto.BadPaddingException e) {
            JOptionPane.showMessageDialog(this, "Error: Incorrect password or file corrupted (BadPaddingException).");
        } catch (javax.crypto.IllegalBlockSizeException e) {
            JOptionPane.showMessageDialog(this, "Error: Incorrect password or file corrupted (IllegalBlockSizeException).");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading or decompressing file: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + e.getMessage());
        }
    }
            
    private Cipher initCipher(String password, int mode) throws Exception {
        byte[] key = Arrays.copyOf(MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8")), 16);
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(mode, secretKey);
        return cipher;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileCompressionGUI frame = new FileCompressionGUI();
            frame.setVisible(true);
        });
    }
}

class PasswordDialog extends JDialog {
    private JPasswordField passwordField;
    private JButton okButton;
    private JButton skipButton;
    private String password = null;

    public PasswordDialog(JFrame parentFrame) {
        super(parentFrame, "Set Password", true);
        setLayout(new FlowLayout());
        setSize(300, 150);
        setLocationRelativeTo(parentFrame);

        add(new JLabel("Set Password:"));
        passwordField = new JPasswordField(20);
        add(passwordField);

        okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            password = new String(passwordField.getPassword());
            setVisible(false);
        });
        add(okButton);

        skipButton = new JButton("Skip");
        skipButton.addActionListener(e -> {
            password = null;
            setVisible(false);
        });
        add(skipButton);
    }

    public String getPassword() {
        return password;
    }
}
