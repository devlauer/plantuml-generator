File buildLog = new File( basedir, 'build.log' )
assert buildLog.exists()
assert buildLog.text.contains( "[INFO] Starting plantuml generation" )
File buildDiagram = new File( basedir.absolutePath+'/target/generated-docs/testdiagram1.txt' )
assert buildDiagram.exists()
assert buildDiagram.text.contains( "BaseAbstractClass" )
assert buildDiagram.text.contains( "BaseInterface" )
assert buildDiagram.text.contains( "ChildA" )
assert buildDiagram.text.contains( "ChildB" )
assert buildDiagram.text.contains( "Util" )
assert buildDiagram.text.contains( "FileEntry" )
assert buildDiagram.text.contains( "[plantuml")
