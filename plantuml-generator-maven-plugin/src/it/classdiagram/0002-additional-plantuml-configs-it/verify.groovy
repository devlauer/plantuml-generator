File buildLog = new File( basedir, 'build.log' )
assert buildLog.exists()
assert buildLog.text.contains( "[INFO] Starting plantuml generation" )

File buildDiagram = new File( basedir.absolutePath+'/target/generated-docs/testdiagram1.txt' )
File testDiagram = new File( basedir.absolutePath+'/../../../../../plantuml-generator-util/src/test/resources/class/0023_additional-plant-uml-configs.txt' )
assert buildDiagram.exists()
assert testDiagram.exists()
String buildDiagramText = buildDiagram.text 
String testDiagramText =  testDiagram.text
assert testDiagramText.replaceAll("\\s+", "").equals(buildDiagramText.replaceAll("\\s+", "")) 
