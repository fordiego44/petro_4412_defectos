package pe.com.petroperu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipTool {

	String[] nameSource;
	String[] pathSource;
	String pathTo;
	// TICKET 9000003992
	Logger LOGGER = LoggerFactory.getLogger(getClass());
	// TICKET 9000004510
	List<String> nombresArchivos;
	List<byte[]> archivos;
	// FIN TICKET

	public ZipTool(String[] pathSource, String pathTo) {
		this.pathSource = pathSource;
		this.pathTo = pathTo;
	}

	public ZipTool(String[] nameSource, String[] pathSource, String pathTo) {
		this.nameSource = nameSource;
		this.pathSource = pathSource;
		this.pathTo = pathTo;
	}

	// TICKET 9000004510
	public ZipTool(String[] nameSource, String[] pathSource, String pathTo, List<String> nombresArchivos, List<byte[]> archivos) {
		this.nameSource = nameSource;
		this.pathSource = pathSource;
		this.pathTo = pathTo;
		this.archivos = archivos;
		this.nombresArchivos = nombresArchivos;
	}
	// FIN TICKET

	public void zip() {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] zip");
		// System.out.println("Path to:" + this.pathTo);
		this.LOGGER.info("[INFO] zip " + " This is info  : " + this.pathTo);

		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		try {
			fos = new FileOutputStream(this.pathTo);
			zos = new ZipOutputStream(fos);

			for (String path : pathSource) {
				// TICKET 9000003992
				// System.out.println("Path source:" + path);
				this.LOGGER.info("[INFO] zip " + " This is info : Path source" + path);
				File file = new File(path);
				zos.putNextEntry(new ZipEntry(file.getName()));
				byte[] bytes = Files.readAllBytes(Paths.get(path));
				zos.write(bytes);
				zos.closeEntry();
			}
			zos.close();
			// TICKET 9000003992
			fos.close();
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] zip " + " This is error : " + e);
		} // TICKET 9000003992
		finally {
			if (fos != null) {
				try {
					fos.close();
					this.LOGGER.info("[INFO] zip " + " This is info: se libero el arhivo fos");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] zip" + " This is error : " + e);
				}
			}
			if (zos != null) {
				try {
					zos.close();
					this.LOGGER.info("[INFO] zip " + " This is info: se libero el arhivo zos");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] zip" + " This is error : " + e);
				}
			}
		}

		// TICKET 9000003992
		this.LOGGER.info("[FIN] Zip");
	}


	public void zip2() {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] zip2");
		// System.out.println("Path to:" + this.pathTo);
		this.LOGGER.info("[INFO] " + " This is info  : Path to " + this.pathTo);
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		try {
			fos = new FileOutputStream(this.pathTo);
			zos = new ZipOutputStream(fos);
			for (int i = 0; i < pathSource.length; i++) {
				String path = pathSource[i];
				// TICKET 9000003992
				// System.out.println("Path source:" + path);
				this.LOGGER.info("[INFO]  zip2 " + " This is info : Path source" + path);
				File file = new File(path);
				zos.putNextEntry(new ZipEntry(nameSource[i]));
				byte[] bytes = Files.readAllBytes(Paths.get(path));
				zos.write(bytes);
				zos.closeEntry();
			}
			zos.close();
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] zip2 " + " This is error : " + e);
			// e.printStackTrace();
		}
		// TICKET 9000003992
		finally {
			if (fos != null) {
				try {
					fos.close();
					this.LOGGER.info("[INFO] zip2 " + " This is info: se libero el arhivo fos");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] zip2 " + " This is error : " + e);
				}
			}
			if (zos != null) {
				try {
					zos.close();
					this.LOGGER.info("[INFO] zip2 " + " This is info: se libero el arhivo zos");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] zip2 " + " This is error : " + e);
				}
			}
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] Zip2 ");
	}


	public void zip3() {
		this.LOGGER.info("[INICIO] zip3");
		this.LOGGER.info("[INFO] " + " This is info  : Path to " + this.pathTo);
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		try {
			fos = new FileOutputStream(this.pathTo);
			zos = new ZipOutputStream(fos);
			this.LOGGER.info("Local:" + pathSource.length + " - Filenet:" + archivos.size());
			for (int i = 0; i < pathSource.length; i++) {
				String path = pathSource[i];
				this.LOGGER.info("[INFO]  zip3 " + " This is info : Path source" + path);
				File file = new File(path);
				zos.putNextEntry(new ZipEntry(nameSource[i]));
				byte[] bytes = Files.readAllBytes(Paths.get(path));
				zos.write(bytes);
				zos.closeEntry();
			}
			for (int i = 0; i < archivos.size(); i++){
				zos.putNextEntry(new ZipEntry(nombresArchivos.get(i)));
				byte[] bytes = archivos.get(i);
				zos.write(bytes);
				zos.closeEntry();
			}
			zos.close();
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] zip3 " + " This is error : " + e);
		}
		finally {
			if (fos != null) {
				try {
					fos.close();
					this.LOGGER.info("[INFO] zip3 " + " This is info: se libero el arhivo fos");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] zip3 " + " This is error : " + e);
				}
			}
			if (zos != null) {
				try {
					zos.close();
					this.LOGGER.info("[INFO] zip3 " + " This is info: se libero el arhivo zos");
				} catch (IOException e) {
					this.LOGGER.error("[ERROR] zip3 " + " This is error : " + e);
				}
			}
		}
		this.LOGGER.info("[FIN] Zip3 ");
	}

}
