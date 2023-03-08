package main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JSON_Reader {
    
    private String[] skillFiles = new String[]{"app/TimeTableSkill.json"};
    private JSONObject[] skillObjects = new JSONObject[1];

    public JSON_Reader(){

    }

    public void loadSkills() {

        try {
            Reader reader;
            int index = 0;
            for (String skillFile : skillFiles) {
                JSONParser parser = new JSONParser();
                reader = new FileReader(skillFile);

                skillObjects[index] = (JSONObject) parser.parse(reader);

                reader.close();
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject[] getSkillObjects() {
        return skillObjects;
    }
}
