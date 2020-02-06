/* 
 * Copyright 2015 Charles Allen Schultz II.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.claflin.cyfinder.internal;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.work.TaskManager;

/**
 * Contains Global Constants.
 * 
 * @author Charles Allen Schultz II
 * @version 1.1 June 17, 2015
 */
public class Global {
    
    /**
     * Package member used for accessing cytoscape functionality in other 
     * classes.  Used to access the CySwingApplication service.
     */
    static CySwingApplication desktopService = null;
    /**
     * Package member used for accessing cytoscape functionality in other 
     * classes.  Used to access the TaskManager service.
     */
    static TaskManager taskManagerService = null;
    /**
     * Package member used for accessing cytoscape functionality in other 
     * classes.  Used to access the CyRootNetworkManager service;
     */
    static CyRootNetworkManager rootNetworkService = null;
    /**
     * Package member used for accessing cytoscape functionality in other 
     * classes.  Used to access the CyNewtorkManager service.
     */
    static CyNetworkManager networkManagerService = null;
    
    /**
     * Gets the Cytoscape Desktop Service.
     * 
     * @return the CySwingApplication object of the current cytoscape instance.
     */
    public static CySwingApplication getDesktopService() {
        return desktopService;
    }
    
    /**
     * Gets the Cytoscape TaskManager Service
     * 
     * @return the TaskManager object of the current cytoscape instance.
     */
    public static TaskManager getTaskManagerService() {
        return taskManagerService;
    }
    
    /**
     * Gets the Cytoscape RootNetwork service.
     * 
     * @return the CyRootNetworkManager object of the current cytoscape instance.
     */
    public static CyRootNetworkManager getRootNetworkService() {
        return rootNetworkService;
    }
    
    /**
     * Gets teh Cytoscape NetworkManager service.
     * 
     * @return the CyNetworkManager object of the current cytoscape instance.
     */
    public static CyNetworkManager getNetworkManagerService() {
        return networkManagerService;
    }
}
