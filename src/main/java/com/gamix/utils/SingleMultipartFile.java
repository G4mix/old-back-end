package com.gamix.utils;

import jakarta.servlet.http.Part;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SingleMultipartFile implements MultipartFile {
    private final Part part;

    public SingleMultipartFile(Part part) {
        this.part = part;
    }

    @org.jetbrains.annotations.NotNull
    @NotNull
    @Override
    public String getName() {
        return this.part.getName();
    }

    @Override
    public String getOriginalFilename() {
        return this.part.getSubmittedFileName();
    }

    @Override
    public String getContentType() {
        return this.part.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return (this.part.getSize() == 0);
    }

    @Override
    public long getSize() {
        return this.part.getSize();
    }

    @org.jetbrains.annotations.NotNull
    @NotNull
    @Override
    public byte[] getBytes() throws IOException {
        return FileCopyUtils.copyToByteArray(this.part.getInputStream());
    }

    @org.jetbrains.annotations.NotNull
    @NotNull
    @Override
    public InputStream getInputStream() throws IOException {
        return this.part.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        this.part.write(dest.getPath());
        if (dest.isAbsolute() && !dest.exists()) {
            FileCopyUtils.copy(this.part.getInputStream(), Files.newOutputStream(dest.toPath()));
        }
    }

    @Override
    public void transferTo(@org.jetbrains.annotations.NotNull Path dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.part.getInputStream(), Files.newOutputStream(dest));
    }
}