package org.apache.clusterbr.zupportl5.processor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemTreeFileExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SystemTreeFileExecutor.class);

    public static void main(String[] args) {

        if (args.length < 1) {
            logger.error("(SystemTreeFileExecutor::main) No file path specified.");
            System.exit(1);
        }

        String command, msg, osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("win"),
                isUnix = osName.contains("nix") || osName.contains("nux") || osName.contains("mac");

        try {

            String outputFilePath = args[0];

            if (isWindows) {
                command = "cmd.exe /c ver && tree /A /F";
            } else if (isUnix) {
                command = "uname -a && pwd && tree";
            } else {
                msg = String.format("(SystemTreeFileExecutor) Unsupported OS: %s", osName);
                logger.warn(msg);
                System.out.println(msg);
                return;
            }

            String line1;
            int lineNumber = 0;
            Process process = Runtime.getRuntime().exec(command);
            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line1 = reader.readLine()) != null) {
                output.append(line1).append("\n");
                // -- log n-first lines
                if (lineNumber <= 6) {
                    logger.info(line1);
                }
                lineNumber++;
            }

            boolean writeCmdOutputToFile = (lineNumber > 6);

            if (writeCmdOutputToFile) {
                File outputFILE = new File(outputFilePath);
                try (Writer writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(outputFILE), StandardCharsets.UTF_8))) {
                    writer.write(output.toString());
                }
                msg = String.format("(SystemTreeFileExecutor) writing output to file: %s | size: %s bytes",
                        outputFilePath, String.valueOf(outputFILE.getTotalSpace()));
                logger.info(msg);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (SystemTreeFileExecutor)", ex);
        }
    }
}
