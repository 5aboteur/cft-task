#### To run the program you should follow these steps:

1. Download source files from the remote link:
`git clone https://github.com/5aboteur/cft-task.git`
2. Go to the `target` directory and run:
`java -jar <JAR> <OPTIONS>` where `JAR` is the .jar file and `OPTIONS` are
* `<path>` -- path to the directory with files you want to sort; 
* `--out-prefix=<pfx>` -- string that will be added as prefix to the previous filename;
* `--content-type=<type>` -- type of sorted content (`i` for integer, `s` for strings);
* `--sort-mode=<mode>` -- sort order (`a` for ascending, `d` for descending)
* `--workers=<n>` -- number of worker threads (default=2).

Or you can open that project in Maven environment and build the package by yourself.

The .jar file was build for targets which have version of Java >= 1.8 and was tested
with the following version:

`$ java -version`

`openjdk version "11.0.3" 2019-04-16`

`OpenJDK Runtime Environment (build 11.0.3+7-Ubuntu-1ubuntu218.04.1)`

`OpenJDK 64-Bit Server VM (build 11.0.3+7-Ubuntu-1ubuntu218.04.1, mixed mode, sharing)`

