Get-ChildItem 'C:\Users\Pichler Martin\Desktop\BlueSoft\Diplomarbeit\stream\' -recurse | where-object {$_.length -gt 5242880} | Sort-Object length | ft fullname, length -auto > .\.gitignore