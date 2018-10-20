package com.jdm.main;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.StreamSupport;

public class FileSizeConverter {
	
	private final static String[] IEX_PREFIXES = {"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};
	
	public static void main(String[] args) {
		StreamSupport.stream(FileSystems.getDefault()
				.getRootDirectories()
				.spliterator(), true)
				.map(FileSizeConverter::driveSizeRatio)
				.forEach(System.out::println);
	}
	
	private static String driveSizeRatio(Path path) {
		try {
			FileStore store = Files.getFileStore(path);
			String descriptor = path.toString();
			double free = store.getUnallocatedSpace();
			double total = store.getTotalSpace();
			free /= total;
			free *= 100D;
			descriptor += " " + ((long) free) + "%";
			int prefix = Math.min(prefix(store.getUsableSpace()), prefix(store.getTotalSpace()));
			descriptor += String.format(" (%s/%s %s)", justNumber(store.getUsableSpace(), prefix), justNumber(store.getTotalSpace(), prefix), IEX_PREFIXES[prefix]);
			return descriptor;
		} catch (IOException e) {
			return e.toString();
		}
	}
	
	private static long justNumber(long bytes, int prefix) {
		if (bytes < 0) {
			throw new IllegalArgumentException("Number of bytes must be positive");
		} else if (bytes == 0) {
			return 0;
		}
		int calPrefix = prefix(bytes);
		double value = bytes;
		for (int i = 0; i < Math.min(calPrefix, prefix); i++) {
			value /= 1024D;
		}
		return (long) value;
	}
	
	private static int prefix(long bytes) {
		return bytes == 0 ? 0 :((int) (Math.log((double) bytes) / Math.log(2D))) / 10;
	}
}