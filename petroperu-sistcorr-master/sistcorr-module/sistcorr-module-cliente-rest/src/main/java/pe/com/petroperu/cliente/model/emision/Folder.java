package pe.com.petroperu.cliente.model.emision;

public class Folder
{
  public String rootFolderId;
  public String newFolderName;
  
  public Folder() {}
  
  public Folder(String rootFolderId, String newFolderName) {
    this.rootFolderId = rootFolderId;
    this.newFolderName = newFolderName;
  }


  
  public String toString() { return "Folder [rootFolderId=" + this.rootFolderId + ", newFolderName=" + this.newFolderName + "]"; }
}
