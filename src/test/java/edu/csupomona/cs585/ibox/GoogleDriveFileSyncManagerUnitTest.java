package edu.csupomona.cs585.ibox;

import edu.csupomona.cs585.ibox.sync.*;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Delete;
import com.google.api.services.drive.Drive.Files.Insert;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.Drive.Files.Update;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


public class GoogleDriveFileSyncManagerUnitTest {

	Drive mockDrive;
	GoogleDriveFileSyncManager driveFSManager;
	java.io.File mockLocalFile;
	File body;
	Files mockFiles;
	FileList fileList;
	ArrayList<File> arrayListFile;
	List mockList;
	
	@Before
	public void setUp(){
		
		mockDrive = Mockito.mock(Drive.class);
		driveFSManager = new GoogleDriveFileSyncManager(mockDrive);
		mockLocalFile = Mockito.mock(java.io.File.class);
		mockFiles = Mockito.mock(Files.class);
		
		body = new File();
		
		fileList = new FileList();
		arrayListFile = new ArrayList<File>();
		
		mockList = Mockito.mock(List.class);
	}
	@Test
	public void testAdd() throws IOException{
		
		body.setId("Id001");

		Insert mockInsert = Mockito.mock(Insert.class);
		
		Mockito.when(mockDrive.files()).thenReturn(mockFiles);
		Mockito.when(mockFiles
					.insert(Mockito.isA(File.class), Mockito.isA(FileContent.class)))
					//insert(body, mediaContent)
					.thenReturn(mockInsert);
		
		Mockito.when(mockInsert.execute()).thenReturn(body);
		
		driveFSManager.addFile(mockLocalFile);

		assertEquals("Id001", body.getId());
		Mockito.verify(mockInsert).execute();
		
	}

	
	@Test
	public void testUpdate() throws IOException{

		body.setId("Id002");
		body.setTitle("Title");

		Update mockUpdate = Mockito.mock(Update.class);

		arrayListFile.add(body);
		fileList.setItems(arrayListFile);

		Mockito.when(mockLocalFile.getName()).thenReturn("Title");

		Mockito.when(mockDrive.files()).thenReturn(mockFiles);
		
		Mockito.when(mockFiles.update(
						Mockito.isA(String.class), 
						Mockito.isA(File.class), 
						Mockito.isA(FileContent.class))).thenReturn(mockUpdate);
		Mockito.when(mockUpdate.execute()).thenReturn(body);
		
		Mockito.when(mockFiles.list()).thenReturn(mockList);
		Mockito.when(mockList.execute()).thenReturn(fileList);
		
		driveFSManager.updateFile(mockLocalFile);
		
		Mockito.verify(mockUpdate).execute();
		assertEquals("Id002", body.getId());

	}

	@Test
	public void testDelete() throws IOException{
		
		body.setId("Id003");
		body.setTitle("Title");
		
		arrayListFile.add(body);
		fileList.setItems(arrayListFile);
		
		Delete mockDelete = Mockito.mock(Delete.class);
		
		Mockito.when(mockLocalFile.getName()).thenReturn("Title");
		Mockito.when(mockList.execute()).thenReturn(fileList);
		Mockito.when(mockDrive.files()).thenReturn(mockFiles);
		Mockito.when(mockFiles.list()).thenReturn(mockList);
		
		Mockito.when(mockFiles.delete(Mockito.isA(String.class))).thenReturn(mockDelete);
		Mockito.when(mockDelete.execute()).thenReturn(null);
		
		driveFSManager.deleteFile(mockLocalFile);
		
		Mockito.verify(mockDelete).execute();

	}
}
