package map;

import core.Game;
import java.io.*;
import java.util.*;

import static com.raylib.Colors.WHITE;
import static com.raylib.Raylib.DrawTexture;

public class Board {
    private Terrain[][] map;
    private final Game game;
    private static final Map<String, Terrain> TERRAIN_MAP = createTerrainMap();

    public Board(Game game) {
        this.game = game;
        loadMap();
    }

    public Terrain[][] getMap() {
        return map;
    }

    public void setMap(String terrain,int x,int y) {
        map[x][y] = TERRAIN_MAP.getOrDefault(terrain, new Terrain(TerrainEnum.GRASS));
    }

    private static Map<String, Terrain> createTerrainMap() {
        Map<String, Terrain> map = new HashMap<>();
        for (TerrainEnum terrainEnum : TerrainEnum.values()) {
            map.put(terrainEnum.name()
                            .replace("STREET_CURVE_DOWN_LEFT", "Street_c_d_l")
                            .replace("STREET_CURVE_DOWN_RIGHT", "Street_c_d_r")
                            .replace("STREET_CURVE_UP_LEFT", "Street_c_u_l")
                            .replace("STREET_CURVE_UP_RIGHT", "Street_c_u_r")
                            .replace("STREET_HORIZONTAL_DOWN", "Street_h_d")
                            .replace("STREET_HORIZONTAL_UP", "Street_h_u")
                            .replace("STREET_HORIZONTAL", "Street_h")
                            .replace("STREET_VERTICAL_RIGHT", "Street_v_r")
                            .replace("STREET_VERTICAL_LEFT", "Street_v_l")
                            .replace("STREET_VERTICAL", "Street_v")
                            .replace("GRASS", "Grass")
                            .replace("FOREST", "Forest")
                            .replace("MOUNTAIN", "Mountain")
                            .replace("STREET", "Street"),
                    new Terrain(terrainEnum));
        }
        return map;
    }

    public void loadMap(){
        List<String[]> data = readCSV(game.getMapPath());
        map = buildMap(data);
    }

    public Terrain[][] buildMap(List<String[]> csv){

        if(csv.isEmpty()) return new Terrain[0][0];

        int rows = csv.size();
        int cols = csv.getFirst().length;
        Terrain[][] data = new Terrain[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = TERRAIN_MAP.getOrDefault(csv.get(i)[j], new Terrain(TerrainEnum.GRASS));
            }
        }


        return data;
    }

    public List<String[]> readCSV(String filePath) {
        List<String[]> dataList  = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                dataList .add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    public void drawMap(){

        for(int j = 0; j <= map.length-1; j++) {
            for(int i = 0; i <= map[j].length-1; i++) {
                DrawTexture(map[j][i].getTexture(), i*32, j*32, WHITE);
            }
        }

    }




}
