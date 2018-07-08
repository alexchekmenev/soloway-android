/*
 * Copyright (c) 2011-2018 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.guessme.app;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPolyline;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapPolyline;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * This class encapsulates the properties and functionality of the Map view.
 */
public class MapFragmentView {
    private MapFragment m_mapFragment;
    private Activity m_activity;
    private Map m_map;

    public MapFragmentView(Activity activity) {
        m_activity = activity;
        initMapFragment();
    }

    private void initMapFragment() {
        /* Locate the mapFragment UI element */
        m_mapFragment = (MapFragment) m_activity.getFragmentManager()
                .findFragmentById(R.id.mapfragment);

        // Set path of isolated disk cache
        String diskCacheRoot = Environment.getExternalStorageDirectory().getPath()
                + File.separator + ".isolated-here-maps";
        // Retrieve intent name from manifest
        String intentName = "";
        try {
            ApplicationInfo ai = m_activity.getPackageManager().getApplicationInfo(m_activity.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            intentName = bundle.getString("INTENT_NAME");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.getClass().toString(), "Failed to find intent name, NameNotFound: " + e.getMessage());
        }

        boolean success = true;//MapSettings.setIsolatedDiskCacheRootPath(diskCacheRoot, intentName);
        if (!success) {
            // Setting the isolated disk cache was not successful, please check if the path is valid and
            // ensure that it does not match the default location
            // (getExternalStorageDirectory()/.here-maps).
            // Also, ensure the provided intent name does not match the default intent name.
        } else {
            if (m_mapFragment != null) {
            /* Initialize the MapFragment, results will be given via the called back. */
                m_mapFragment.init(new OnEngineInitListener() {
                    @Override
                    public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {

                        if (error == Error.NONE) {
                        /*
                         * If no error returned from map fragment initialization, the map will be
                         * rendered on screen at this moment.Further actions on map can be provided
                         * by calling Map APIs.
                         */
                            m_map = m_mapFragment.getMap();

                            /*
                             * Map center can be set to a desired location at this point.
                             * It also can be set to the current location ,which needs to be delivered by the PositioningManager.
                             * Please refer to the user guide for how to get the real-time location.
                             */

                            int cnt = 5;
                            double[] latitudes = new double[cnt];
                            double[] longitudes = new double[cnt];

                            latitudes[0] = 45.03344100;
                            longitudes[0] = 41.96618500;

                            latitudes[1] = 45.04093200;
                            longitudes[1] = 41.97287700;

                            latitudes[2] = 45.04329700;
                            longitudes[2] = 41.96783400;

                            latitudes[3] = 45.04469200;
                            longitudes[3] = 41.96813500;

                            latitudes[4] = 45.04529800;
                            longitudes[4] = 41.96888300;

                            List<GeoCoordinate> coords = new ArrayList<GeoCoordinate>();
                            for(int i = 0; i < cnt; i++) {
                                coords.add(new GeoCoordinate(latitudes[i], longitudes[i]));
                            }
                            Log.d("GUEESSME", ""+coords.size());
                            m_map.setCenter(coords.get(cnt / 2), Map.Animation.NONE);
                            GeoPolyline geoPolyline = new GeoPolyline(coords);
                            m_map.addMapObject(new MapPolyline(geoPolyline));
//                            m_map.zoomTo(line.getBoundingBox(), Map.Animation.LINEAR, 1);
                            Log.d("GUESSME", m_map.getMaxZoomLevel()+"");
                        } else {

                            Toast.makeText(m_activity,
                                    "ERROR: Cannot initialize Map with error " + error,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    /* Creates a route from 4350 Still Creek Dr to Langley BC with highways disallowed */
//    private void createRoute() {
//        /* Initialize a CoreRouter */
//        CoreRouter coreRouter = new CoreRouter();
//
//        /* Initialize a RoutePlan */
//        RoutePlan routePlan = new RoutePlan();
//
//        /*
//         * Initialize a RouteOption.HERE SDK allow users to define their own parameters for the
//         * route calculation,including transport modes,route types and route restrictions etc.Please
//         * refer to API doc for full list of APIs
//         */
//        RouteOptions routeOptions = new RouteOptions();
//        /* Other transport modes are also available e.g Pedestrian */
//        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
//        /* Disable highway in this route. */
//        routeOptions.setHighwaysAllowed(false);
//        /* Calculate the shortest route available. */
//        routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
//        /* Calculate 1 route. */
//        routeOptions.setRouteCount(1);
//        /* Finally set the route option */
//        routePlan.setRouteOptions(routeOptions);
//
//        /* Define waypoints for the route */
//        /* START: 4350 Still Creek Dr */
//        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(49.259149, -123.008555));
//        /* END: Langley BC */
//        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(49.073640, -122.559549));
//
//        /* Add both waypoints to the route plan */
//        routePlan.addWaypoint(startPoint);
//        routePlan.addWaypoint(destination);
//
//        /* Trigger the route calculation,results will be called back via the listener */
//        coreRouter.calculateRoute(routePlan,
//                new Router.Listener<List<RouteResult>, RoutingError>() {
//                    @Override
//                    public void onProgress(int i) {
//                        /* The calculation progress can be retrieved in this callback. */
//                    }
//
//                    @Override
//                    public void onCalculateRouteFinished(List<RouteResult> routeResults,
//                                                         RoutingError routingError) {
//                        /* Calculation is done.Let's handle the result */
//                        if (routingError == RoutingError.NONE) {
//                            if (routeResults.get(0).getRoute() != null) {
//                                /* Create a MapRoute so that it can be placed on the map */
//                                m_mapRoute = new MapRoute(routeResults.get(0).getRoute());
//
//                                /* Show the maneuver number on top of the route */
//                                m_mapRoute.setManeuverNumberVisible(true);
//
//                                /* Add the MapRoute to the map */
//                                m_map.addMapObject(m_mapRoute);
//
//                                /*
//                                 * We may also want to make sure the map view is orientated properly
//                                 * so the entire route can be easily seen.
//                                 */
//                                GeoBoundingBox gbb = routeResults.get(0).getRoute()
//                                        .getBoundingBox();
//                                m_map.zoomTo(gbb, Map.Animation.NONE,
//                                        Map.MOVE_PRESERVE_ORIENTATION);
//                            } else {
//                                Toast.makeText(m_activity,
//                                        "Error:route results returned is not valid",
//                                        Toast.LENGTH_LONG).show();
//                            }
//                        } else {
//                            Toast.makeText(m_activity,
//                                    "Error:route calculation returned error code: " + routingError,
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
}