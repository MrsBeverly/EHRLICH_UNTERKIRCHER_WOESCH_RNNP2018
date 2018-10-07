/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
package Blatt1;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Aufgabe2 {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Invalid Number of Arguments");
        }

        final String inputFilename = args[0];
        final String outputFilename = args[1];

        // trying to open input file
        try (Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilename), Charset.forName("UTF-8")))) {

            // trying to open output
            try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outputFilename)), Charset.forName("ISO-8859-1"))) {

                // put entry into zip output
                out.putNextEntry(new ZipEntry(Paths.get(inputFilename).getFileName().toString()));

                // read and write with replacement of LF with CRLF
                ReadWriteWithLFtoCRLF(in, out);

                // close the zip entry
                out.closeEntry();

            } catch (FileNotFoundException e) {
                System.err.println(e);
            }

        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void ReadWriteWithLFtoCRLF(Reader in, OutputStream out) throws IOException {

        for (int character = in.read(); character != -1; character = in.read()) {
            if (character == '\n') {
                // replace LF with CRLF
                out.write('\r');
            }
            out.write(character);
        }
    }
}

