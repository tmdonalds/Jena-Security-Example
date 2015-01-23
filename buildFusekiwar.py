__author__ = 'trevor'

import os.path
import shutil

basedir=os.getcwd()

workDir         = basedir + '/build/'
cleanFusekiWar  = workDir + 'clean/fuseki.war'
shiroExtJar     = basedir + '/fuseki-shiro-web/target/fuseki-shiro-web-1.0-SNAPSHOT.jar'
distDir         = basedir + '/dist'
libDir          = workDir + 'WEB-INF/lib'

tomcatDir       = '/home/trevor/servers/tomcat/apache-tomcat-7.0.57/webapps'

if(os.path.exists(tomcatDir + '/fuseki.war')) :
    os.remove(tomcatDir + '/fuseki.war')

assert os.path.exists(shiroExtJar)
assert os.path.exists(cleanFusekiWar)

shutil.copy(shiroExtJar, libDir)
shutil.copy(cleanFusekiWar, workDir)

os.chdir(workDir)

for i in os.listdir(libDir):
    os.system("jar -uf ./fuseki.war WEB-INF/lib/"+i)

shutil.copy("./fuseki.war",distDir)
os.system("cp ./fuseki.war "+tomcatDir)
os.remove(workDir + 'fuseki.war')