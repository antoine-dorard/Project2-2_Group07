/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package main;

public class App {

    /**
     * App starting point
     */
    public void init(){
        new Frame();
        //new JSON_Reader().loadSkills();
        // TODO: 0. bot answer computation in parallel threads
        // TODO: 1. write discussion to json file (e.g. save name)
        // TODO: 2. spelling checking (e.g. all caps, spelling mistakes checking)
    }

    public static void main(String[] args) {
        new App().init();
    }
}
