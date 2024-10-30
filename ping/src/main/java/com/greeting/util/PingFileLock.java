package com.greeting.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;


/**
 * @author Sam
 * @date 2014-10-29
 */
@Slf4j
public class PingFileLock implements Closeable {


    private static final Integer MAX_REQUEST_LIMIT = 2;
    private static final Integer MAX_RETRY_TIMES = 5;


    private Boolean lock = false;
    private Integer currentValue = 0;

    private RandomAccessFile raf;
    private RandomAccessFile lockRaf;
    private FileLock         fileLock;

    private Long currentDateSecond = System.currentTimeMillis() / 1000;


    //disabled default constructor
    private PingFileLock() {
    }

    public PingFileLock(String lockFile,String valueFileName) throws IOException, AcquireFileLockException {
        // Incompatible folder creation, file only.
        createDoesNotExist(lockFile);
        createDoesNotExist(valueFileName);

        this.lockRaf = new RandomAccessFile(lockFile, "rw");
        this.raf = new RandomAccessFile(valueFileName, "rw");
        //try lock with timeout
        this.fileLock = tryLock(lockRaf,false);

        if (this.fileLock != null) {
            this.currentValue = readCounter(this.raf);
            if (MAX_REQUEST_LIMIT <= this.currentValue) {
                // out of limit
                lock = false;
            } else {
                this.currentValue += 1;
                writeCounter(this.raf, currentValue);
                log.info("Incremented value: " + currentValue);
            }
        } else {
            log.error("Getting file lock failed.");
            throw new AcquireFileLockException("Getting file lock failed.");
        }
    }

    private int readCounter(RandomAccessFile raf) throws IOException {
        raf.seek(0);
        Long dateSecond = 0L;
        Integer count    = 0;
        try {
            String line = raf.readLine();
            System.out.println("line:" + line);
            if(StringUtils.hasText(line) && line.indexOf(",") > 0){
                String[] data = line.split(",");
                dateSecond = Long.parseLong(data[0]);
                if(dateSecond.equals(this.currentDateSecond)){
                    count = Integer.parseInt(data[1]);
                }
            }
            return count;
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    private void writeCounter(RandomAccessFile raf, int value) throws IOException {
        raf.setLength(0); // Clear existing content
        String finalValue = this.currentDateSecond + "," + value;
        System.out.println("write:" + finalValue);

        raf.write(finalValue.getBytes());
    }

    private FileLock tryLock(RandomAccessFile raf,boolean shared)  {
        FileLock fileLock = null;
        Integer  retryTimes = 0;
        while (true) {
            try {
                if (retryTimes++ < MAX_RETRY_TIMES) {
                    fileLock = raf.getChannel().tryLock(0, Long.MAX_VALUE, shared);//共享锁
                    if (fileLock != null) {
                        this.lock = true;
                        break;
                    } else {
                        Thread.sleep(100); //timeout
                    }
                } else {
                    break;
                }
            } catch (Exception e) {
                log.debug("There are other threads operating the file. The current thread: {}", Thread.currentThread().getName());
            }
        }

        return fileLock;
    }


    @Override
    public void close() throws IOException {

        //close all stream
        if(this.raf != null){
            this.raf.close();
        }

        if(this.lockRaf != null){
            this.lockRaf.close();
        }
    }

    public Boolean getLock() {
        return lock;
    }


    public static void createDoesNotExist(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
