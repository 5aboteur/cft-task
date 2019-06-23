#### To run the program you should follow these steps:

1. Download the sources from the remote link:
`git clone https://github.com/5aboteur/cft-task.git`
2. Then go to the `bin` folder and run in the terminal:
`jar cfmv sort-it.jar manifest.txt app/`
3. This should create a .jar archive, so the next step will be:
`java -jar sort-it.jar <OPTIONS>` where `OPTIONS` is
* `<path>` -- an absolute path to the directory with files you want to sort; 
* `--out-prefix=<pfx>` -- a string that will be concatenated with previous filename;
* `--content-type=<type>` -- a type of sorted content (`i` for integer, `s` for strings);
* `--sort-mode=<mode>` -- the way to sort content (`a` for ascending, `d` for descending).

#### Java version >= 1.8.0_212
