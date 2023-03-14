/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package main;

public class App {

    SkillLoader skillLoader;
    /**
     * App starting point
     */
    public void init(){
        new Frame(this);
        skillLoader = new SkillLoader();
        skillLoader.loadSkills(new String[]{"calendar", "weather"});
        System.out.println(skillLoader.getQuestions().toString());

        // TODO: 0. bot answer computation in parallel threads
        // TODO: 1. write discussion to json file (e.g. save name)
        // TODO: 2. spelling checking (e.g. all caps, spelling mistakes checking)
    }

    public SkillLoader getSkillLoader() {
        return skillLoader;
    }

    public static void main(String[] args) {
        if (App.class.getResource("/GradleResourceChecker") == null){
            System.err.println("Error: Please run the program using Gradle ( > Gradle run )");
            return;
        }
        new App().init();
    }
}
