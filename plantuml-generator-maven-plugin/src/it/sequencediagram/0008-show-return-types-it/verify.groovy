File buildLog = new File( basedir, 'build.log' )
assert buildLog.exists()
assert buildLog.text.contains( "[INFO] Starting plantuml sequence diagram generation" )

File buildDiagram = new File( basedir.absolutePath+'/target/generated-docs/testsequencediagram1.txt' )
File testDiagram = new File( basedir.absolutePath+'/../../../../../plantuml-generator-util/src/test/resources/sequence/0001_basic_caller_test_with_return_types.txt' )
assert buildDiagram.exists()
assert testDiagram.exists()
String buildDiagramText = buildDiagram.text 
String testDiagramText =  testDiagram.text
assert testDiagramText.replaceAll("\\s+", "").equals(buildDiagramText.replaceAll("\\s+", "")) 
