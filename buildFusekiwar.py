__author__ = 'trevor'

import os.path
import shutil

srcfile = '/home/trevor/IdeaProjects/Jena-Security-Parent/fuseki-shiro-web/target/fuseki-shiro-web-1.0-SNAPSHOT.jar'
dstdir = '/home/trevor/servers/fuseki/jena-fuseki-dist-2.0.0-SNAPSHOT/work/WEB-INF/lib'

warSrcFile = '/home/trevor/servers/fuseki/jena-fuseki-dist-2.0.0-SNAPSHOT/fuseki.war'
fusekiWorkDir = '/home/trevor/servers/fuseki/jena-fuseki-dist-2.0.0-SNAPSHOT/work'

assert os.path.exists(srcfile)
assert os.path.exists(warSrcFile)

shutil.copy(srcfile, dstdir)
shutil.copy(warSrcFile, fusekiWorkDir)

os.system("jar -uf ./fuseki.war WEB-INF/lib/fuseki-shiro-web-1.0-SNAPSHOT.jar")