package com.mygdx.game.Utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class MapAdapter {

    private static TiledMap tiledMap;
    private int tileWidth = 32;
    private int tileHeight = 32;

    public MapAdapter(TiledMap tiledMap) {
        MapAdapter.tiledMap = tiledMap;
    }

    public int getMapWidthInTiles() {
        return tiledMap.getProperties().get("width", Integer.class);
    }

    public int getMapHeightInTiles() {
        return tiledMap.getProperties().get("height", Integer.class);
    }

    public int getTileWidthInPixels() {
        return tiledMap.getProperties().get("tilewidth", Integer.class);
    }

    public int getTileHeightInPixels() {
        return tiledMap.getProperties().get("tileheight", Integer.class);
    }

    public int getMapWidthInPixels() {
        return getMapWidthInTiles() * getTileWidthInPixels();
    }

    public int getMapHeightInPixels() {
        return getMapHeightInTiles() * getTileHeightInPixels();
    }

    public int getGroundTileValue(int tileX, int tileY) {
        if (tiledMap != null) {
            TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("ground");

            if (tileX >= 0 && tileX < layer.getWidth() && tileY >= 0 && tileY < layer.getHeight()) {
                TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

                if (cell != null && cell.getTile() != null) {
                    return cell.getTile().getId();
                }
            }
        }

        return 0;
    }

    public int getSpikesTileValue(int tileX, int tileY) {
        if (tiledMap != null) {
            TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("spikes");

            if (tileX >= 0 && tileX < layer.getWidth() && tileY >= 0 && tileY < layer.getHeight()) {
                TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

                if (cell != null && cell.getTile() != null) {
                    return cell.getTile().getId();
                }
            }
        }

        return 0;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
