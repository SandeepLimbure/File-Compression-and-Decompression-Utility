package Projects;

import java.io.*;
import java.util.*;


public class HuffmanCompressor {
    static class Node implements Comparable<Node>{
        char ch;
        int freq;
        Node left,right;
        public Node(char ch , int freq){
            this.ch=ch;
            this.freq=freq;
            this.left=null;
            this.right=null;
        }
        @Override
        public int compareTo(Node other) {
            return this.freq-other.freq;
        }
    }
    public static HashMap<Character,Integer> readInputFile(String filePath){
        HashMap<Character, Integer> freqMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int charCode;
            while ((charCode = reader.read()) != -1) {
                char c = (char) charCode;
                freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return freqMap;
    }

    public static PriorityQueue<Node> buildPriorityQueue(HashMap<Character,Integer> freqMap){
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }
        return pq;
    }
    public static Node buildHuffmanTree(PriorityQueue<Node> pq){
        if (pq.size() == 1) {
            Node leaf = pq.poll();
            Node parent = new Node('\0', leaf.freq);
            parent.left = leaf;
            return parent;
        }
        while(pq.size()>1){
            Node left = pq.poll();
            Node right = pq.poll();
            int sumFreq = left.freq+ right.freq;
            Node parent = new Node('\0',sumFreq);
            parent.left=left;
            parent.right=right;
            pq.add(parent);
        }
        return pq.poll();
    }
    public static void generateCodes(Node root,String code,HashMap<Character,String> codeMap){
        if(root==null){
            return;
        }
        if(root.left==null && root.right==null){
            codeMap.put(root.ch,code.isEmpty() ? "0" : code);
            return;
        }
        generateCodes(root.left,code+"0",codeMap);
        generateCodes(root.right,code+"1",codeMap);
    }
    public static HashMap<Character,String> getHuffmanCodes(Node root){
        HashMap<Character,String> codeMap = new HashMap<>();
        generateCodes(root,"",codeMap);
        return codeMap;
    }
    public static void compressFile(String inputPath, String outputPath, HashMap<Character, Integer> freqMap, HashMap<Character, String> codeMap) {
        StringBuilder encoded = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            int charCode;
            while ((charCode = reader.read()) != -1) {
                encoded.append(codeMap.get((char) charCode));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(outputPath))) {
            out.writeInt(freqMap.size());
            for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
                out.writeChar(entry.getKey());
                out.writeInt(entry.getValue());
            }
            int bitCount = 0;
            byte currentByte = 0;
            for (char bit : encoded.toString().toCharArray()) {
                currentByte = (byte) (currentByte << 1);
                if (bit == '1') currentByte |= 1;
                bitCount++;
                if (bitCount == 8) {
                    out.writeByte(currentByte);
                    currentByte = 0;
                    bitCount = 0;
                }
            }
            if (bitCount > 0) {
                currentByte = (byte) (currentByte << (8 - bitCount));
                out.writeByte(currentByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static HashMap<Character,Integer> readFrequencyTable(String inputPath){
        HashMap<Character,Integer> freqMap = new HashMap<>();
        try {
            DataInputStream  in = new DataInputStream(new FileInputStream(inputPath));
            int size = in.readInt();
            for(int i = 1;i<=size;i++){
                char c = in.readChar();
                int freq = in.readInt();
                freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return freqMap;
    }
    public static StringBuilder readBinaryData(String inputPath, int headerSize){
        StringBuilder bits = new StringBuilder();
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(inputPath));
            int byteRead;
            while((byteRead=in.read())!=-1){
                for(int i = 1;i>0;i--){
                    int bit = (byteRead >> i)&1;
                    bits.append(bit);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bits;
    }
    public static void decompressFile(String inputPath, String outputPath) {
        HashMap<Character, Integer> freqMap = new HashMap<>();
        int totalChars = 0;
        try (DataInputStream in = new DataInputStream(new FileInputStream(inputPath))) {
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                char c = in.readChar();
                int freq = in.readInt();
                freqMap.put(c, freq);
                totalChars += freq;
            }
            PriorityQueue<Node> pq = buildPriorityQueue(freqMap);
            Node root = buildHuffmanTree(pq);
            StringBuilder bits = new StringBuilder();
            int byteRead;
            while ((byteRead = in.read()) != -1) {
                for (int i = 7; i >= 0; i--) {
                    bits.append((byteRead >> i) & 1);
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                Node current = root;
                int decodedChars = 0;
                for (char bit : bits.toString().toCharArray()) {
                    if (current == null) break;
                    current = bit == '0' ? current.left : current.right;
                    if (current != null && current.left == null && current.right == null) {
                        writer.write(current.ch);
                        decodedChars++;
                        current = root;
                        if (decodedChars >= totalChars) break; // Stop after decoding all characters
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error decompressing file: " + e.getMessage());
        }
    }

    static void displayStats(String inputPath, String compressedPath) {
        File inputFile = new File(inputPath);
        File compressedFile = new File(compressedPath);
        long originalSize = inputFile.length();
        long compressedSize = compressedFile.length();
        double ratio = (originalSize - compressedSize) / (double) originalSize * 100;
        System.out.println("Original Size: " + originalSize + " bytes");
        System.out.println("Compressed Size: " + compressedSize + " bytes");
        System.out.println("Compression Ratio: " + ratio + "%");
    }
    static boolean compareFiles(String file1, String file2) {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(file1));
             BufferedReader reader2 = new BufferedReader(new FileReader(file2))) {
            String line1, line2;
            while ((line1 = reader1.readLine()) != null && (line2 = reader2.readLine()) != null) {
                if (!line1.equals(line2)) return false;
            }
            return reader1.readLine() == null && reader2.readLine() == null;
        } catch (IOException e) {
            System.err.println("Error comparing files: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        String[][] tests = {
                {"normal.txt", "hello world"},
                {"repetitive.txt", "aaaaabbbbb"},
                {"single.txt", "a"},
                {"empty.txt", ""},
                {"large.txt", new String(new char[1024]).replace("\0", "a") + new String(new char[1024]).replace("\0", "b")}
        };

        for (String[] test : tests) {
            String inputPath = test[0];
            String content = test[1];
            String compressedPath = "output.huff";
            String decompressedPath = "decompressed.txt";

            System.out.println("\nTesting: " + inputPath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputPath))) {
                writer.write(content);
            } catch (IOException e) {
                System.err.println("Error creating test file: " + e.getMessage());
                continue;
            }

            HashMap<Character, Integer> freqMap = readInputFile(inputPath);
            if (freqMap.isEmpty() && !inputPath.equals("empty.txt")) {
                System.out.println("Test failed: Frequency map is empty for non-empty file");
                continue;
            }
            if (inputPath.equals("empty.txt")) {
                System.out.println("Input file is empty ");
                continue;
            }

            PriorityQueue<Node> pq = buildPriorityQueue(freqMap);
            Node root = buildHuffmanTree(pq);
            HashMap<Character, String> codeMap = getHuffmanCodes(root);
            compressFile(inputPath, compressedPath, freqMap, codeMap);
            displayStats(inputPath, compressedPath);
            decompressFile(compressedPath, decompressedPath);

            boolean filesMatch = compareFiles(inputPath, decompressedPath);
            System.out.println("Decompression complete: " + decompressedPath);
            System.out.println("Test " + (filesMatch ? "passed" : "failed") + ": " +
                    (filesMatch ? "Decompressed file matches original" : "Decompressed file does not match original"));

            new File(inputPath).delete();
            new File(compressedPath).delete();
            new File(decompressedPath).delete();
        }
    }
}
