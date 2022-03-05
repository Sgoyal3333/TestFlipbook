On Error Resume Next
  Dim fso, folder, files, NewsFile,sFolder
  
  Set fso = CreateObject("Scripting.FileSystemObject")
  sFolder = "C:\Users\srathore\git\WM.COM_1\wmcanada\src\test\java\wm\wmcanada\testscripts\CMS"
  If sFolder = "" Then
      Wscript.Echo "No Folder parameter was passed"
      Wscript.Quit
  End If
  Set NewFile = fso.CreateTextFile(sFolder&"\FileList.txt", True)
  Set folder = fso.GetFolder(sFolder)
  Set files = folder.Files
  
  For each folderIdx In files
    NewFile.WriteLine(folderIdx.Name)
  Next
  NewFile.Close
