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

import edu.claflin.cyfinder.internal.tasks.factories.MakeUndirectedTaskFactory;
import edu.claflin.cyfinder.internal.tasks.factories.SubgraphFinderTaskFactory;
import java.util.Properties;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.task.NetworkTaskFactory;
import static org.cytoscape.work.ServiceProperties.ENABLE_FOR;
import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;
import org.cytoscape.work.TaskManager;
import org.osgi.framework.BundleContext;

/**
 * Activates the plugin in Cytoscape.
 *
 * @author Charles Allen Schultz II
 * @version 1.4.1 June 19, 2015
 */
public class CyFinderActivator extends AbstractCyActivator {

    /**
     * {@inheritDoc }
     * 
     * @param context the BundleContext of the currently running Cytoscape.
     * @throws Exception any and all exceptions. :P
     */
    @Override
    public void start(BundleContext context) throws Exception {
        Global.desktopService = getService(context, CySwingApplication.class);
        Global.taskManagerService = getService(context, TaskManager.class);
        Global.rootNetworkService = getService(context, CyRootNetworkManager.class);
        Global.networkManagerService = getService(context, CyNetworkManager.class);
        
        // Subgraph Finder Service
        Properties finderProps = new Properties();
        finderProps.put(TITLE, "Subgraph Finder");
        finderProps.put(PREFERRED_MENU, "Apps.CyFinder");
        finderProps.put(ENABLE_FOR, "network");
        
        SubgraphFinderTaskFactory sfTaskFactory = 
                new SubgraphFinderTaskFactory();
        
        registerService(context, sfTaskFactory, NetworkTaskFactory.class, 
                finderProps);
        
        // Make Undirected Service - Additive
        Properties undirectedPropsAdditive = new Properties();
        undirectedPropsAdditive.put(TITLE, "Additive Method");
        undirectedPropsAdditive.put(PREFERRED_MENU, "Apps.CyFinder.Make Undirected");
        undirectedPropsAdditive.put(ENABLE_FOR, "network");
        
        MakeUndirectedTaskFactory muaTaskFactory = 
                new MakeUndirectedTaskFactory(true);
        
        registerService(context, muaTaskFactory, NetworkTaskFactory.class,
                undirectedPropsAdditive);
        
        // Make Undirected Service - Transformative
        Properties undirectedPropsTransformative = new Properties();
        undirectedPropsTransformative.put(TITLE, "Transformative Method");
        undirectedPropsTransformative.put(PREFERRED_MENU, "Apps.CyFinder.Make Undirected");
        undirectedPropsTransformative.put(ENABLE_FOR, "network");
        
        MakeUndirectedTaskFactory mutTaskFactory = 
                new MakeUndirectedTaskFactory(false);
        
        registerService(context, mutTaskFactory, NetworkTaskFactory.class,
                undirectedPropsTransformative);
    }
}
