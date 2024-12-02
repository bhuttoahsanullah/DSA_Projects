# File Compressor (Compresso)

# Project Overview
This project is a file compression tool with an integrated encryption feature. It uses the Lempel-Ziv-Welch (LZW) algorithm to reduce file size efficiently and provides optional encryption using AES (Advanced Encryption Standard) to secure files. The project includes a user-friendly Graphical User Interface (GUI) built with Java Swing, enabling easy interaction for compressing and decompressing files.

# Features
**File Compression:** Reduces file size using the LZW algorithm.
**File Decompression:** Restores files to their original state.
**Encryption:** Secures compressed files with AES encryption (password-protected).
**User-Friendly GUI:** Simple interface for browsing, compressing, and decompressing files.

# How to Run the Project
**Prerequisites**
**Java Development Kit (JDK):**
Ensure JDK 8 or higher is installed on your system.
Add java and javac to your system's PATH environment variable.

# Required Files:
Download the project folder, which includes:
FileCompressionGUI.java
LZWCompressor.java
LZWDecompressor.java
Supporting .class files and icons.

# Steps to Run
**Option 1: Using an IDE **
Open an Integrated Development Environment (IDE) like IntelliJ IDEA, Eclipse, or VS Code.
Import the project folder into the IDE.
Compile all the .java files.
Run the FileCompressionGUI class.

**Option 2: Using the Command Line**
Navigate to the project folder in the command prompt or terminal.
Compile all Java files:
bash
javac *.java
Run the main program:
bash
java FileCompressionGUI

**How to Use the Application**
Launch the Application:

Run the program to open the GUI.
Browse and Select a File:

Click on the "Browse File" button to choose a file from your system.
Compress the File:

Click "Compress".
Set a password for encryption (optional) or skip it for regular compression.
The compressed file will be saved with a .zip extension.
Decompress the File:

Click "Decompress".
If the file was encrypted, enter the correct password to decrypt it. Otherwise, proceed without a password.
The decompressed file will be restored to its original state.

# Important Notes
Ensure the selected file is accessible and not in use by another application.
The project supports text-based and binary files but works best with text files for higher compression efficiency.
Encrypted files can only be decompressed with the correct password.

Project Links
GitHub Repository: https://github.com/bhuttoahsanullah/File_Compressor_DSA_Project.git
