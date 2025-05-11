# Huffman Compressor

## Overview
Huffman Compressor is a Java-based implementation of the Huffman coding algorithm, a lossless data compression technique. This project compresses text files by assigning variable-length codes to characters based on their frequency, resulting in smaller file sizes. It also supports decompression, restoring the original file without data loss. The project includes a testing suite to verify compression and decompression accuracy across various input scenarios.

## Features
- **Compression**: Compresses text files using Huffman coding, reducing file size efficiently.
- **Decompression**: Restores compressed files to their original content.
- **Frequency Analysis**: Analyzes character frequencies in the input file to build an optimal Huffman tree.
- **Statistics Display**: Shows original and compressed file sizes along with the compression ratio.
- **Automated Testing**: Tests the compressor with various inputs, including normal, repetitive, single-character, empty, and large files.
- **File Comparison**: Verifies that decompressed files match the original files.

## Prerequisites
To run this project, you need:
- **Java Development Kit (JDK)**: Version 8 or higher.
- **Java Runtime Environment (JRE)**: To execute the compiled program.
- A command-line interface (e.g., Terminal, Command Prompt) or an IDE like IntelliJ IDEA or Eclipse.

## Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/huffman-compressor.git
   cd huffman-compressor
   ```
   Replace `your-username` with your GitHub username.

2. **Compile the Java Code**:
   Ensure you're in the project directory, then compile the source code:
   ```bash
   javac Projects/HuffmanCompressor.java
   ```

3. **Verify Setup**:
   Check that the `HuffmanCompressor.class` file is generated in the `Projects` directory.

## Usage
1. **Running the Program**:
   Execute the program using the following command:
   ```bash
   java Projects.HuffmanCompressor
   ```
   The program runs a series of automated tests with predefined input files (`normal.txt`, `repetitive.txt`, `single.txt`, `empty.txt`, `large.txt`). Each test:
   - Creates a test file with specific content.
   - Compresses it to `output.huff`.
   - Decompresses it to `decompressed.txt`.
   - Displays compression statistics (original size, compressed size, compression ratio).
   - Compares the original and decompressed files to verify correctness.
   - Cleans up by deleting temporary files.

2. **Custom Input Files**:
   To compress a custom file, modify the `main` method in `HuffmanCompressor.java` to include your file path and content, or create a new method to handle custom inputs. Example:
   ```java
   String inputPath = "myfile.txt";
   String compressedPath = "myfile.huff";
   String decompressedPath = "myfile_decompressed.txt";
   HashMap<Character, Integer> freqMap = readInputFile(inputPath);
   PriorityQueue<Node> pq = buildPriorityQueue(freqMap);
   Node root = buildHuffmanTree(pq);
   HashMap<Character, String> codeMap = getHuffmanCodes(root);
   compressFile(inputPath, compressedPath, freqMap, codeMap);
   decompressFile(compressedPath, decompressedPath);
   displayStats(inputPath, compressedPath);
   ```

3. **Output**:
   - Compression statistics are printed to the console.
   - Test results indicate whether the decompressed file matches the original (`Test passed` or `Test failed`).

## File Structure
```
huffman-compressor/
├── Projects/
│   └── HuffmanCompressor.java  # Main Java source file
├── README.md                   # Project documentation (this file)
└── (Temporary files)           # Generated during execution: normal.txt, output.huff, decompressed.txt, etc.
```

## How It Works
1. **Frequency Analysis**: Reads the input file and counts the frequency of each character.
2. **Huffman Tree Construction**: Builds a priority queue of nodes (characters and their frequencies) and constructs a Huffman tree by repeatedly combining the two nodes with the lowest frequencies.
3. **Code Generation**: Assigns binary codes to characters based on their position in the Huffman tree (left = 0, right = 1).
4. **Compression**: Converts the input file into a binary string using the generated codes and writes it to a compressed file along with a frequency table.
5. **Decompression**: Reads the frequency table, rebuilds the Huffman tree, and decodes the binary data to reconstruct the original file.
6. **Verification**: Compares the original and decompressed files to ensure accuracy.

## Limitations
- **Text Files Only**: The current implementation is optimized for text files. Binary files may require modifications.
- **Empty Files**: Empty files are handled but produce no compressed output.
- **Single-Character Files**: Handled with a special case to ensure a valid Huffman tree.
- **Error Handling**: Basic error handling is implemented; extensive error scenarios may need additional checks.

## Testing
The project includes automated tests for:
- **Normal Text**: A file with varied characters (`hello world`).
- **Repetitive Text**: A file with repeated characters (`aaaaabbbbb`).
- **Single Character**: A file with one character (`a`).
- **Empty File**: An empty file.
- **Large File**: A file with 2048 characters (1024 'a's and 1024 'b's).

Each test verifies compression, decompression, and file integrity. Results are printed to the console.

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit (`git commit -m "Add feature"`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

Please ensure your code follows the existing style and includes appropriate tests.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact
For questions or suggestions, open an issue on GitHub or contact the maintainer at [your-email@example.com](mailto:your-email@example.com).