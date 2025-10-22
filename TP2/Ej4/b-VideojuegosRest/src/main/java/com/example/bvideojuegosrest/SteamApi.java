package com.example.bvideojuegosrest;

import com.example.bvideojuegosrest.services.VideogameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * SteamApi is a Spring component that can be injected and used to download
 * the global app list and fetch details for a subset of apps. Converting
 * it into a bean allows it to call other services (like VideogameService)
 * to immediately process the downloaded data.
 */
@Component
public class SteamApi {

    private final VideogameService videogameService;

    public SteamApi(VideogameService videogameService) {
        this.videogameService = videogameService;
    }

    // Downloads the full app list to allGames.json if missing, then fetches details.
    public void getGames() throws IOException, InterruptedException {
        File archivo = new File("allGames.json");
        if (archivo.exists() && !archivo.isDirectory()) {
            System.out.println("allGames.json ya existe — omitiendo descarga.");
            getFewGames();
        } else {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.steampowered.com/ISteamApps/GetAppList/v2/"))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String json = response.body();
                // usamos la variable 'archivo' creada arriba
                ObjectMapper mapper = new ObjectMapper();
                Object data = mapper.readValue(json, Object.class);
                mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, data);

                System.out.println("✅ Guardado en allGames.json");
                getFewGames();
            } else {
                System.out.println("❌ Error " + response.statusCode());
            }
        }
    }

    // Fetches a limited number of app details and writes fewGames.json containing only {appid,data}.
    public void getFewGames() {
        // This method reads `allGames.json` (list of appids), requests details for the
        // first N appids from the Steam store API and writes a cleaned `fewGames.json`.
        // The output contains only entries with a successful response and keeps only
        // the inner `data` object for easier downstream processing.
        ObjectMapper mapper = new ObjectMapper();
        File archivo = new File("allGames.json");
        File archivo2 = new File("fewGames.json");

        if (!archivo.exists()) {
            System.out.println("Archivo allGames.json no encontrado. Ejecutá getGames() primero.");
            return;
        }
        if (!archivo2.exists()) {
            try {
                // Leer estructura: { "applist": { "apps": [ {"appid":..., "name":"..."}, ... ] } }
                @SuppressWarnings("unchecked")
                Map<String, Object> root = mapper.readValue(archivo, Map.class);
                Object applistObj = root.get("applist");
                if (!(applistObj instanceof Map)) {
                    System.out.println("Formato inesperado de allGames.json: no contiene 'applist' como objeto.");
                    return;
                }

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> apps = (List<Map<String, Object>>)
                        ((Map<String, Object>) applistObj).get("apps");

                if (apps == null || apps.isEmpty()) {
                    System.out.println("No se encontraron apps en allGames.json");
                    return;
                }


                int limit = Math.min(200, apps.size());
                List<Integer> appIds = new ArrayList<>();

                // If the total app list is larger than the limit, sample randomly
                // across the full list so we don't only pick the very first N items.
                if (apps.size() <= limit) {
                    for (int i = 0; i < apps.size(); i++) {
                        Object idObj = apps.get(i).get("appid");
                        if (idObj instanceof Number) {
                            appIds.add(((Number) idObj).intValue());
                        } else if (idObj instanceof String) {
                            try {
                                appIds.add(Integer.parseInt((String) idObj));
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                } else {
                    // Build a list of indices, shuffle and pick first `limit` indices
                    List<Integer> indices = new ArrayList<>();
                    for (int i = 0; i < apps.size(); i++) indices.add(i);
                    java.util.Collections.shuffle(indices);
                    for (int j = 0; j < limit; j++) {
                        int idx = indices.get(j);
                        Object idObj = apps.get(idx).get("appid");
                        if (idObj instanceof Number) {
                            appIds.add(((Number) idObj).intValue());
                        } else if (idObj instanceof String) {
                            try {
                                appIds.add(Integer.parseInt((String) idObj));
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }

                if (appIds.isEmpty()) {
                    System.out.println("No se obtuvieron appids válidos");
                    return;
                }
                appIds.addFirst(1030300);

                List<Map<String, Object>> results = new ArrayList<>();
                HttpClient client = HttpClient.newHttpClient();

                int successCount = 0;
                int skippedCount = 0;
                int errorCount = 0;

                for (int appid : appIds) {
                    try {
                        String base = "https://store.steampowered.com/api/appdetails?appids=" + appid;
                        String url1 = base + "&l=es";
                        String url2 = base;
                        //String url3 = base + "&l=es&cc=us";

                        String[] candidates = new String[]{url1, url2};
                        HttpResponse<String> resp = null;
                        boolean entryHandled = false;

                        for (String url : candidates) {
                            try {
                                HttpRequest req = HttpRequest.newBuilder()
                                        .uri(URI.create(url))
                                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0 Safari/537.36")
                                        .GET()
                                        .build();

                                resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                                if (resp.statusCode() != 200) {
                                    continue; // try next candidate
                                }

                                @SuppressWarnings("unchecked")
                                Map<String, Object> body = mapper.readValue(resp.body(), Map.class);
                                Object entry = body.get(String.valueOf(appid));
                                if (entry instanceof Map) {
                                    @SuppressWarnings("unchecked")
                                    Map<String, Object> entryMap = (Map<String, Object>) entry;
                                    Object successObj = entryMap.get("success");
                                    boolean success = (successObj instanceof Boolean && (Boolean) successObj) || "true".equals(String.valueOf(successObj));
                                    if (success) {
                                        Object dataObj = entryMap.get("data");
                                        // Ensure dataObj is a Map and type == "game"
                                        if (dataObj instanceof Map) {
                                            @SuppressWarnings("unchecked")
                                            Map<String, Object> dataMap = (Map<String, Object>) dataObj;
                                            Object typeObj = dataMap.get("type");
                                            String type = typeObj == null ? "" : String.valueOf(typeObj);
                                            if (!"game".equalsIgnoreCase(type)) {
                                                // not a game (music, dlc, etc.)
                                                skippedCount++;
                                                entryHandled = true;
                                                break;
                                            }

                                            // Check for NSFW indicators in genres/categories/content_descriptors/tags
                                            boolean nsfw = false;
                                            String[] nsfwKeywords = new String[]{"nudity", "sexual", "porn", "adult", "erotic", "explicit", "nsfw", "mature"};

                                            // helper to inspect lists/maps for keywords
                                            java.util.function.Predicate<Object> containsNsfw = obj -> {
                                                if (obj == null) return false;
                                                String txt = String.valueOf(obj).toLowerCase();
                                                for (String k : nsfwKeywords) if (txt.contains(k)) return true;
                                                return false;
                                            };

                                            // genres
                                            Object genresObj = dataMap.get("genres");
                                            if (genresObj instanceof List) {
                                                @SuppressWarnings("unchecked")
                                                List<Map<String, Object>> genres = (List<Map<String, Object>>) genresObj;
                                                for (Map<String, Object> g : genres) {
                                                    if (containsNsfw.test(g.get("description"))) { nsfw = true; break; }
                                                }
                                            }

                                            // categories
                                            if (!nsfw) {
                                                Object categoriesObj = dataMap.get("categories");
                                                if (categoriesObj instanceof List) {
                                                    @SuppressWarnings("unchecked")
                                                    List<Map<String, Object>> cats = (List<Map<String, Object>>) categoriesObj;
                                                    for (Map<String, Object> c : cats) {
                                                        if (containsNsfw.test(c.get("description"))) { nsfw = true; break; }
                                                    }
                                                }
                                            }

                                            // content_descriptors
                                            if (!nsfw) {
                                                Object cdObj = dataMap.get("content_descriptors");
                                                if (cdObj instanceof Map) {
                                                    @SuppressWarnings("unchecked")
                                                    Map<String, Object> cd = (Map<String, Object>) cdObj;
                                                    // sometimes there is a 'notes' or similar
                                                    for (Object v : cd.values()) {
                                                        if (containsNsfw.test(v)) { nsfw = true; break; }
                                                    }
                                                }
                                            }

                                            // tags (some responses include tags as a map or string)
                                            if (!nsfw) {
                                                Object tagsObj = dataMap.get("tags");
                                                if (tagsObj != null) {
                                                    if (tagsObj instanceof Map) {
                                                        @SuppressWarnings("unchecked")
                                                        Map<String, Object> tags = (Map<String, Object>) tagsObj;
                                                        for (Object v : tags.keySet()) {
                                                            if (containsNsfw.test(v)) { nsfw = true; break; }
                                                        }
                                                    } else {
                                                        if (containsNsfw.test(tagsObj)) nsfw = true;
                                                    }
                                                }
                                            }

                                            if (nsfw) {
                                                // skip NSFW entries
                                                skippedCount++;
                                                entryHandled = true;
                                                break;
                                            }

                                            // Passed filters: add to results
                                            Map<String, Object> out = new HashMap<>();
                                            out.put("appid", appid);
                                            out.put("data", dataMap);
                                            results.add(out);
                                            successCount++;
                                            entryHandled = true;
                                            break;
                                        } else {
                                            // data is not a map; skip
                                            skippedCount++;
                                            entryHandled = true;
                                            break;
                                        }
                                    } else {
                                        skippedCount++;
                                        entryHandled = true;
                                        break;
                                    }
                                }
                                // otherwise try next candidate
                            } catch (Exception innerEx) {
                                // try next candidate
                                continue;
                            }
                        }

                        if (!entryHandled) {
                            if (resp != null) {
                                Map<String, Object> out = new HashMap<>();
                                out.put("appid", appid);
                                out.put("httpStatus", resp.statusCode());
                                results.add(out);
                                errorCount++;
                            } else {
                                Map<String, Object> out = new HashMap<>();
                                out.put("appid", appid);
                                out.put("note", "no data or success=false (all candidates tried)");
                                results.add(out);
                                skippedCount++;
                            }
                        }

                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }

                    } catch (Exception e) {
                        Map<String, Object> out = new HashMap<>();
                        out.put("appid", appid);
                        out.put("exception", e.getMessage());
                        results.add(out);
                    }
                }

                // Escribir resultados
                File outFile = new File("fewGames.json");
                mapper.writerWithDefaultPrettyPrinter().writeValue(outFile, results);
                System.out.println("✅ Guardado en fewGames.json -> " + results.size() + " entradas");
                System.out.println("Resumen: success=" + successCount + ", skipped(success=false)=" + skippedCount + ", errors=" + errorCount + ", attempted=" + appIds.size());

            } catch (IOException e) {
                System.out.println("Error leyendo allGames.json: " + e.getMessage());
            }
        }

        // Once fewGames.json exists we ask the videogame service to load/process it.
        try {
            videogameService.loadVideogames();
        } catch (Exception e) {
            System.out.println("Error al cargar videojuegos desde fewGames.json: " + e.getMessage());
        }
    }
}
