// build and Result objects
import hudson.model.*

/**
 * VARIABLES
 */

def config = new HashMap()
def bindings = getBinding()
config.putAll(bindings.getVariables())

//def out = config['out']
//Jenkins environment variables 
def envVarsMap = build.parent.builds[0].properties.get("envVars")
config.putAll(envVarsMap)

class GenerateService {
	GenerateService(config){
	def out = config['out']
	def workspace = config['WORKSPACE']
	def givenServiceName = config['SERVICE_NAME']
	out.println ("workspace :: " + workspace)
	out.println ("given service name :: " + givenServiceName)
	
//    static void main(String[] args) {
	
        //def inputServiceName = "Swbservicea"
        def inputServiceName = "Swbservicea"
        /*if (args.length >0 && args[0] != null){
            inputServiceName = args[0]
        }*/
	if(null != givenServiceName){
		inputServiceName = givenServiceName
	}	
        def dirToRename = []
        def filesToRename = []
        def serviceDirName = "servicename"
        def fileServiceName = "__ServiceName_"
        def tobeServiceDirName = inputServiceName.toLowerCase()
        def tobeFileServiceName = inputServiceName

      //  def genSourceDir = new File("generate")
      //  genSourceDir.mkdir()

       def SOURCE_TEMPLATE_DIR = "/var/lib/jenkins/jobs/test1/workspace/"
	if(workspace != null){
		SOURCE_TEMPLATE_DIR = workspace
	}
     // def SOURCE_TEMPLATE_DIR = $WORKSPACE
        // def TARGET_SERVICE_GEN_DIR = "/Users/abhishekg/temp/sandbox/generateservice"
       // def TARGET_SERVICE_GEN_DIR = "generate"



     //   def targetToClear = new File(TARGET_SERVICE_GEN_DIR)
        //   targetToClear.deleteDir()

       /* new AntBuilder().copy(todir: TARGET_SERVICE_GEN_DIR) {
            fileset(dir: SOURCE_TEMPLATE_DIR) {
                exclude(name: '.DS_Store')
            }
        }*/

      //  new File(TARGET_SERVICE_GEN_DIR).eachFileRecurse() {
        new File(SOURCE_TEMPLATE_DIR).eachFileRecurse() {
                //file-> println file.getAbsolutePath()
                // def filename = file -> file.name
                //file-> println( file.name)
            def file-> file
                //    println(file.isDirectory())
                // def str = "/Users/abhishekg/temp/sandbox/muServiceTemplate/git/hooks"
                //println(str.indexOf(".git"))
                def name = file.getAbsolutePath()
                if(  name.indexOf(".git") == -1 && name.indexOf(".idea") == -1 ) {

                    if(file.isDirectory() &&
                            name.indexOf("build") == -1 &&
                            name.indexOf(".gradle") == -1 && file.name.indexOf(serviceDirName) !=-1){
                        /* def newDirPath = name.replaceAll("servicename", "swbservice")
                         def newDir = new File(newDirPath)
                          def cloneDir = new File(name)
                         //def File = new File()
                         cloneDir.renameTo(newDir)*/

                        dirToRename << name

                    }else if(file.isFile()){
                        filesToRename << name
                        // println(name)
                    }
                }
        }

        filesToRename.each {
            def filePath-> filePath

                def newFilePath = filePath.replaceAll( fileServiceName, tobeFileServiceName)

                def newFile = new File(newFilePath)
                def cloneFile = new File(filePath)
                def fileContent = cloneFile.text
                fileContent = fileContent.replaceAll(fileServiceName, tobeFileServiceName)
                fileContent = fileContent.replaceAll(serviceDirName, tobeServiceDirName)
                //def File = new File()
                //cloneDir.renameTo(newDir)
                newFile.write(fileContent)
                if(cloneFile.getAbsolutePath().indexOf(fileServiceName) != -1){
                    cloneFile.delete()
                }
        }

        dirToRename.each {
            def filePath-> filePath

                def newDirPath = filePath.replaceAll( serviceDirName, tobeServiceDirName)
                def newDir = new File(newDirPath)
                def cloneDir = new File(filePath)
                //def File = new File()
                cloneDir.renameTo(newDir)
        }


//    }
}
}
new GenerateService(config)
