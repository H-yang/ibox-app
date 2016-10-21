package edu.csupomona.cs585.ibox;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.google.api.services.drive.Drive;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;

public class GoogleDriveFileSyncManagerIntTest {
	
	private Drive googleDrive = GoogleDriveServiceProvider.get().getGoogleDriveClient();
	private GoogleDriveFileSyncManager fileManager = new GoogleDriveFileSyncManager(googleDrive);
	private java.io.File file = new java.io.File("/home/user/Desktop/CS585/forIntTest.txt");
	
	@Test
	public void testAdd() throws IOException{
		fileManager.addFile(file);
		String fileId = fileManager.getFileId("forIntTest");
		System.out.println(fileId);
	}
	
	@Test
	public void testUpdate() throws IOException{
		fileManager.updateFile(file);
	}
	
	@Test
	public void testDelete() throws IOException{
		fileManager.deleteFile(file);
		String fileId = fileManager.getFileId("forIntTest");
		assertEquals(fileId, null);
	}
	
	
}
