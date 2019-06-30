package org.gpsmaster;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.gpsmaster.chart.ChartHandler;
import org.gpsmaster.chart.ChartWindow;
import org.gpsmaster.db.DbLayer;
import org.gpsmaster.db.DbTarget;
import org.gpsmaster.device.MoveBikeCompMPT;
import org.gpsmaster.gpsloader.FileDropHandler;
import org.gpsmaster.gpsloader.GpsLoader;
import org.gpsmaster.gpsloader.GpsLoaderFactory;
import org.gpsmaster.gpsloader.GpxLoader;
import org.gpsmaster.gpxpanel.ArrowType;
import org.gpsmaster.gpxpanel.GPXExtension;
import org.gpsmaster.gpxpanel.GPXFile;
import org.gpsmaster.gpxpanel.GPXObject;
import org.gpsmaster.gpxpanel.GPXPanel;
import org.gpsmaster.gpxpanel.ProgressType;
import org.gpsmaster.gpxpanel.Route;
import org.gpsmaster.gpxpanel.Track;
import org.gpsmaster.gpxpanel.Waypoint;
import org.gpsmaster.gpxpanel.WaypointGroup;
import org.gpsmaster.gpxpanel.WaypointGroup.WptGrpType;
import org.gpsmaster.marker.Marker;
import org.gpsmaster.marker.PhotoMarker;
import org.gpsmaster.marker.WaypointMarker;
import org.gpsmaster.marker.WikiMarker;
import org.gpsmaster.online.DownloadGpsies;
import org.gpsmaster.online.DownloadOsm;
import org.gpsmaster.online.GetWikipedia;
import org.gpsmaster.online.OnlineTrack;
import org.gpsmaster.online.UploadGpsies;
import org.gpsmaster.painter.ArrowPainter;
import org.gpsmaster.painter.DirectDistancePainter;
import org.gpsmaster.painter.ProgressPainter;
import org.gpsmaster.painter.StartEndPainter;
import org.gpsmaster.painter.TrackPainter;
import org.gpsmaster.painter.WaypointPainter;
import org.gpsmaster.pathfinder.PathFinder;
import org.gpsmaster.pathfinder.RouteProviderFactory;
import org.gpsmaster.tree.GPXTree;
import org.gpsmaster.tree.GPXTreeRenderer;
import org.gpsmaster.undo.IUndoable;
import org.gpsmaster.undo.UndoAddRoute;
import org.gpsmaster.undo.UndoAddWaypoint;
import org.gpsmaster.undo.UndoRemoveWaypoint;
import org.gpsmaster.undo.UndoSplitTrackSeg;
import org.gpsmaster.widget.DistanceWidget;
import org.gpsmaster.widget.PathFinderWidget;
import org.gpsmaster.widget.ProgressWidget;
import org.gpsmaster.widget.ScalebarWidget;
import org.gpsmaster.dialogs.BrowserLauncher;
import org.gpsmaster.dialogs.CleaningDialog;
import org.gpsmaster.dialogs.DBDialog;
import org.gpsmaster.dialogs.EditPropsDialog;
import org.gpsmaster.dialogs.GenericDownloadDialog;
import org.gpsmaster.dialogs.ImageViewer;
import org.gpsmaster.dialogs.InfoDialog;
import org.gpsmaster.dialogs.NameSearchPanel;
import org.gpsmaster.dialogs.TimeshiftDialog;
import org.gpsmaster.elevation.Corrector;
import org.gpsmaster.elevation.ElevationProvider;
import org.gpsmaster.elevation.MapQuestProvider;
import org.gpsmaster.filehub.FileHub;
import org.gpsmaster.filehub.FileItem;
import org.gpsmaster.filehub.FileSource;
import org.gpsmaster.filehub.MapSource;
import org.gpsmaster.filehub.MapTarget;
import org.gpsmaster.filehub.TransferLogEntry;
import org.gpsmaster.filehub.TransferableItem;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.OsmMercator;
import org.openstreetmap.gui.jmapviewer.OsmTileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOpenAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOsmTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.TemplatedTMSTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.TileSourceInfo;

import com.topografix.gpx._1._1.LinkType;

import eu.fuegenstein.messagecenter.MessageCenter;
import eu.fuegenstein.messagecenter.MessagePanel;
import eu.fuegenstein.swing.CustomColorChooser;
import eu.fuegenstein.swing.NamedColor;
import eu.fuegenstein.swing.WidgetLayout;
import eu.fuegenstein.unit.UnitConverter;
import eu.fuegenstein.unit.UnitFactory;
import eu.fuegenstein.unit.UnitSet;

/**
 * 
 * The main application class for GPS Master, a GUI for analyzing, converting and 
 * manipulating files containing GPS data.<br />
 * 
 * written by Rainer Fügenstein (rfu)
 * Based on GPX Creator by Matt Hoover
 *  
 * More info at
 * 		www.gpsmaster.org 
 * 		www.gpxcreator.com
 * 
 * @author hooverm
 * @author rfuegen
 * 
 */
@SuppressWarnings("serial")
public class GpsMaster extends JComponent {

	public static final String PROGRAM_NAME = "GpsMaster";
	public static final String VERSION_NUMBER = "0.62.97";
	public static final String ME = PROGRAM_NAME + " " + VERSION_NUMBER;
	
    // indents show layout hierarchy
    private JFrame frame;
    private static String lookAndFeel;
    	private JPanel menuBarsPanel;

        private JToolBar toolBarMain;       // NORTH
        	private JButton btnFileNew;                
            private JButton btnFileOpen;
            private JToggleButton tglDownload;
            private JFileChooser chooserFileOpen;
            private JButton btnFileSave;
            private JButton btnPrint;
            private JButton btnUndo;
            private JFileChooser chooserFileSave;
            private File fileSave;
            private JButton btnObjectDelete;
            private JButton btnEditProperties;
            private JToggleButton tglPathFinder;
            private JToggleButton tglAddRoutepoint;
            private JToggleButton tglDelPoints;
            private JToggleButton tglSplitTrackseg;
            private JToggleButton tglMeasure;
            private JToggleButton tglProgress;
            private JToggleButton tglArrows;
            private JButton btnTimeShift;
            private JButton btnCorrectEle;
            private JToggleButton tglChart;
            private JToggleButton tglToolbar;
            private JButton btnInfo;
            private JComboBox<TileSource> comboBoxTileSource;            
            private JLabel lblLat;
            private JTextField textFieldLat;
            private JLabel lblLon;
            private JTextField textFieldLon;
            private JToggleButton tglLatLonFocus;
            private JToggleButton tglAutoFit;
        private JToolBar toolBarSide;
    		private JButton btnCleaning;
        	private JButton btnMergeOneToOne;
        	private JButton btnMergeToMulti;
        	private JButton btnMergeToSingle;
        	private JButton btnMergeParallel;
            private JToggleButton tglAddWaypoint;
        private JToolBar toolBarDownload;
        	private JButton btnDeviceOpen;
        	private JButton btnDatabase;
        	private JButton btnDbSave;
        	private JButton btnDownloadOsm;
        	private JButton btnDownloadGpsies;
        	private JButton btnUploadGpsies;
        	private JButton btndownloadWiki;
        private JSplitPane splitPaneMain;   // CENTER
            private JSplitPane splitPaneSidebar;    // LEFT
                private JPanel containerLeftSidebarTop;        // TOP
                    private JPanel containerExplorerHeading;
                        private JLabel labelExplorerHeading;
                    private JScrollPane scrollPaneExplorer;
                        private GPXTree tree;
                        private NameSearchPanel searchPanel;
                private JPanel containerLeftSidebarBottom;    // BOTTOM
                private JSplitPane splitPaneSideCenter = new JSplitPane();
                    private JPanel containerPropertiesHeading;
                        private JLabel labelPropertiesHeading;
                    private JScrollPane scrollPaneProperties;
                        private PropsTableModel propsTableModel;
                        private JTable tableProperties;
            private JSplitPane splitPaneMap;                
	            private GPXPanel mapPanel;              // RIGHT
	            private ChartWindow chartWindow;

	private Container contentPane;
    private Cursor mapCursor;
    private boolean mouseOverLink;
    private DistanceWidget distanceWidget = null;
    
    private TrackSlider trackSlider = null; // move to correct position above
    private final double mapToChartRatio = 0.85f; // distribution of space between map and chart on the mapPanel
    
    /**
     * @author rfuegen
     */
    private Core core = new Core();
    private MessageCenter msg = null; 
    private Config conf;
    public UnitConverter uc = new UnitConverter();
    
    public static ActiveGpxObjects active = null;
    private String configFilename = "./GpsMaster.config"; // System.getProperty("user.home")
    
    private GpsLoaderFactory loaderFactory = new GpsLoaderFactory();
    private ActivityHandler activityHandler = null;
    private ImageViewer imageViewer = null;
    private MeasureThings measure = null;
    private PathFinder pathFinder = null;
    private PathFinderWidget pathFinderWidget = null;
    private ChartHandler chartHandler = null;
    private ProgressPainter progressPainter = null;
    private ArrowPainter arrowPainter = null;
    private DbLayer db = null;
    
    private PropertyChangeListener propertyListener = null;
    private WindowAdapter windowListener = null;
    private FileDropHandler dropHandler = null;
    
    private MapTarget transferTargetMap = null;
    private FileHub centralFileHub = null;
    
    // List containing all JToggleButtons which are mutually exclusive:
    private List<JToggleButton> toggles = new ArrayList<JToggleButton>();
    private boolean autoFitToPanel = true;
    // globally defined members to be passed as params to SwingWorker() jobs
    private MessagePanel routeInfoPanel = null;
    
    private final Cursor WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
    private final Cursor CROSSHAIR_CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
    private final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    
    // stupid "inner class" global requirements
    private Corrector eleCorr = null;
    private final Color MENU_BACKGROUND = Color.WHITE;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GpsMaster window = new GpsMaster();
                    window.frame.setVisible(true);
                    window.frame.requestFocusInWindow();
                 } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public GpsMaster() {
        initialize();
    }

    /**
     * Initialize the contents of the parentFrame.
     */
    private void initialize() {

    	// final String iconPath = "/org/gpsmaster/icons/";
    	
        /* MAIN FRAME
         * --------------------------------------------------------------------------------------------------------- */
        frame = new JFrame(ME);
        contentPane = frame.getContentPane();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int frameWidth = (screenWidth * 3) / 4;
        int frameHeight = (screenHeight * 3) / 4;
        frame.setBounds(((screenWidth - frameWidth) / 2), ((screenHeight - frameHeight) / 2), frameWidth, frameHeight);
        frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon(GpsMaster.class.getResource(Const.ICONPATH + "gpsmaster.png")).getImage());                
              
        /* CENTRAL PROPERTY CHANGE LISTENER
         * -------------------------------------------------------------------------------------------------------- */        
        propertyListener = new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				handlePropertyChangeEvent(e);
			}
		};
		addPropertyChangeListener(propertyListener);
		
		/* CENTRAL WINDOW/COMPONENT CHANGE LISTENER
         * -------------------------------------------------------------------------------------------------------- */        
		windowListener = new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
				handleWindowEvent(e, WindowEvent.WINDOW_CLOSING);				
			}
        	@Override
        	public void windowClosed(WindowEvent e) {
				handleWindowEvent(e, WindowEvent.WINDOW_CLOSED);
			}
		};

        /* LOAD & APPLY CONFIG
    	 * --------------------------------------------------------------------------------------------------------- */
        msg = new MessageCenter(frame);
		loadConfig();
    	msg.setScreenTime(conf.getScreenTime());

    	setupTree();
    	active = new ActiveGpxObjects(tree);
    	active.addPropertyChangeListener(propertyListener);
    	
    	transferTargetMap = new MapTarget();
    	transferTargetMap.setEnabled(true);
    	transferTargetMap.AddPropertyChangeListener(propertyListener);
    	
    	setupUnits();
        setupPanels();        
        setupMenuBar();
        setupDownloadBar();       
        setupToolbar();
        setupDatabase();
        
        // setup proxy (if configured)
        if ((conf.getProxyHost().isEmpty() == false) && (conf.getProxyPort() > 0)) {
	        java.util.Properties systemProperties = System.getProperties();
	        systemProperties.setProperty("http.proxyHost", conf.getProxyHost());
	        systemProperties.setProperty("http.proxyPort", Integer.toString(conf.getProxyPort()));
        }
        
        if (conf.getActivitySupport()) {
        	activityHandler = new ActivityHandler(mapPanel, contentPane, msg);
    		activityHandler.getWidget().setAlignmentY(TOP_ALIGNMENT);    		
        }
        if (conf.isShowScalebar()) {
        	ScalebarWidget scaleBar = new ScalebarWidget(mapPanel, uc);                
        	mapPanel.add(scaleBar);
        }
        
    	// Set up the globally defined {@link FileHub} used to transfer items from Filesystem to Map    	
		centralFileHub = new FileHub();
		setupFileHub(centralFileHub, "Loading from filesystem ...");
		centralFileHub.setItemSource(new FileSource());
		centralFileHub.addItemTarget(transferTargetMap);
    		
		// handler for dragging / dropping files on the map
        dropHandler = new FileDropHandler(centralFileHub, msg);
        mapPanel.setTransferHandler(dropHandler);
        
        // required for clicks on markers:
        mapPanel.addPropertyChangeListener(propertyListener); 
          
        preload(); // preload chart classes
        
        setupCombo();
    }

    /**
     * 
     */
    private void setupUnits() {
    
    	UnitSet targetSet = new UnitSet();
    	uc.setTargetSet(targetSet);
    	// input set: meters, meters per second
    	uc.setSourceSet(UnitFactory.getMetricSet());    	    	
    	uc.setSymbolPrefix(" ");
    	// TODO bundle this in UnitFactory
    	switch(conf.getUnitSystem()) {
    	case METRIC:
    		UnitFactory.METER.setUpperUnit(UnitFactory.KILOMETER);
    		UnitFactory.KILOMETER.setLowerUnit(UnitFactory.METER);
    		targetSet.setDistanceUnit(UnitFactory.KILOMETER);
    		targetSet.setSpeedUnit(UnitFactory.KILOMETERS_PER_HOUR);
    		targetSet.setElevationUnit(UnitFactory.METER);
    		targetSet.setVerticalSpeedUnit(UnitFactory.METERS_PER_HOUR);
    		break;
    	case IMPERIAL:
    		UnitFactory.FOOT.setUpperUnit(UnitFactory.MILE);
    		UnitFactory.MILE.setLowerUnit(UnitFactory.FOOT);
    		targetSet.setDistanceUnit(UnitFactory.MILE);
    		targetSet.setSpeedUnit(UnitFactory.MILES_PER_HOUR);
    		targetSet.setElevationUnit(UnitFactory.FOOT);
    		targetSet.setVerticalSpeedUnit(UnitFactory.FEET_PER_HOUR);
    		break;
    	case NAUTICAL:
    		UnitFactory.FOOT.setUpperUnit(UnitFactory.NAUTICAL_MILE);
    		UnitFactory.FOOT.setUpperThreshold(6076.12f); // default is for Mile
    		UnitFactory.NAUTICAL_MILE.setLowerUnit(UnitFactory.FOOT);
    		targetSet.setDistanceUnit(UnitFactory.NAUTICAL_MILE);
    		targetSet.setSpeedUnit(UnitFactory.KNOTS);
    		targetSet.setElevationUnit(UnitFactory.FOOT);
    		targetSet.setVerticalSpeedUnit(UnitFactory.FEET_PER_HOUR);
    		break;

    	default:
			throw new UnsupportedOperationException("UnitSystem");
			
    	}    	
    }
    
    /**
     * 
     */
    private void setupTree() {            	
    	
        /* EXPLORER TREE/MODEL
         * --------------------------------------------------------------------------------------------------------- */
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode("GPX Files");
    	DefaultTreeModel treeModel = new DefaultTreeModel(root);
        tree = new GPXTree(treeModel);
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new GPXTreeRenderer());
        tree.putClientProperty("JTree.lineStyle", "None");
        tree.setBackground(Color.white);
        tree.setToggleClickCount(0);
        
        ImageIcon collapsed = new ImageIcon(GpsMaster.class.getResource(Const.ICONPATH_TREE + "tree-collapsed.png"));
        ImageIcon expanded = new ImageIcon(GpsMaster.class.getResource(Const.ICONPATH_TREE + "tree-expanded.png"));
        UIManager.put("Tree.collapsedIcon", collapsed);
        UIManager.put("Tree.expandedIcon", expanded);
        
        // give Java look and feel to tree only (to get rid of dotted line handles/connectors)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            SwingUtilities.updateComponentTreeUI(tree);
        } catch (Exception e) {
            msg.error(e);
        }
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
        	msg.error(e);
        }
                              
        treeModel.addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeStructureChanged(TreeModelEvent e) {}
            @Override
            public void treeNodesRemoved(TreeModelEvent e) {}
            @Override
            public void treeNodesInserted(TreeModelEvent e) {}
            @Override
            public void treeNodesChanged(TreeModelEvent e) { 
            	// necessary for changed color, visibility, trackpoints visibility
                mapPanel.repaint();
            }
        });               
        
        CustomColorChooser chooser = new CustomColorChooser();        
        chooser.setCustomColors(conf.getPalette());
        tree.setColorChooser(chooser);
        tree.addPropertyChangeListener(propertyListener);
        
    }
    
    /**
     * 
     */
	private void setupPanels() {		
		
		/* MENUBARS PANEL
         * --------------------------------------------------------------------------------------------------------- */
		menuBarsPanel = new JPanel();
		menuBarsPanel.setLayout(new BorderLayout());
		contentPane.add(menuBarsPanel, BorderLayout.NORTH);
		
        /* MAIN SPLIT PANE
         * --------------------------------------------------------------------------------------------------------- */
        splitPaneMain = new JSplitPane();
        splitPaneMain.setContinuousLayout(true);
        contentPane.add(splitPaneMain, BorderLayout.CENTER);
        
		/* MAP PANEL
         * --------------------------------------------------------------------------------------------------------- */
        mapPanel = new GPXPanel(uc, msg);
        WidgetLayout layout = new WidgetLayout();
        
    	/* location of widgets at the bottom edge need to be
    	 * a few pixels higher to leave room for map copyright text:
    	 * (maybe even more for message msgPanel(s))
    	 */
        layout.setCornerOffset(WidgetLayout.BOTTOM_LEFT, new Point(0, -20));
        layout.setCornerOffset(WidgetLayout.BOTTOM_RIGHT, new Point(0, -20));
        
        layout.setCornerOrientation(WidgetLayout.BOTTOM_LEFT, WidgetLayout.VERTICAL);
        mapPanel.setLayout(layout);
        mapPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        mapPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mapPanel.setDisplayPosition(new Coordinate(conf.getLat(), conf.getLon()), conf.getPositionZoom());        						
        mapPanel.setZoomContolsVisible(conf.getZoomControls());
        if (mapPanel.getZoomControlsVisible()) {
        	layout.setCornerOffset(WidgetLayout.TOP_LEFT, new Point(35, 0));
        }        

        // initialize track painter 
        TrackPainter trackPainter = new TrackPainter();
        trackPainter.setLineWidth(conf.getTrackLineWidth());
        mapPanel.addPainter(trackPainter);
        
        WaypointPainter wptPainter = new WaypointPainter();
        mapPanel.addPainter(wptPainter);
        
        // TODO add to mapPanel only when necessary
        progressPainter = new ProgressPainter(uc);
        // progressPainter.setDistanceInterval(750);
        mapPanel.addPainter(progressPainter);
        
        // TODO add to mapPanel only when necessary
        arrowPainter = new ArrowPainter();
        mapPanel.addPainter(arrowPainter);
        
        // 
        if (conf.isShowStartEnd()) {
        	mapPanel.addPainter(new StartEndPainter());
        }
        
        try {
            mapPanel.setTileLoader(new OsmTileLoader(mapPanel));
        } catch (Exception e) {
            msg.error("There was a problem constructing the tile cache on disk", e);
        }       
        
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mapPanel.getAttribution().handleAttribution(e.getPoint(), true);
                }
            }
        });
        
        mapCursor = DEFAULT_CURSOR;
        mapPanel.setCursor(mapCursor);
        mapPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean cursorHand = mapPanel.getAttribution().handleAttributionCursor(e.getPoint());
                if (cursorHand) {
                    mapPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    mouseOverLink = true;
                } else {
                    mapPanel.setCursor(mapCursor);
                    mouseOverLink = false;
                }
            }
        });
        
        // up/+ and down/- keys will also zoom the map in and out
        String zoomIn = "zoom in";
        mapPanel.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), zoomIn);
        // keyboard +
        mapPanel.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), zoomIn);
        // numpad +
        mapPanel.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), zoomIn);
        mapPanel.getActionMap().put(zoomIn, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapPanel.zoomIn();
            }
        });
        
        String zoomOut = "zoom out";
        mapPanel.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), zoomOut);
        // keyboard -
        mapPanel.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), zoomOut);
        // numpad -
        mapPanel.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), zoomOut);

        mapPanel.getActionMap().put(zoomOut, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapPanel.zoomOut();
            }
        });
        
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mapPanel.requestFocus();
            }
        });

        /* test
        mapPanel.addJMVListener(new JMapViewerEventListener() {
			
			@Override
			public void processCommand(JMVCommandEvent command) {
				// TODO Auto-generated method stub
				System.out.println("### " + command.getCommand());
			}
		});
        */
        
        /* SIDEBAR SPLIT PANE
        /* --------------------------------------------------------------------------------------------------------- */
        splitPaneSidebar = new JSplitPane();
        splitPaneSidebar.setMinimumSize(new Dimension(240, 25));
        splitPaneSidebar.setPreferredSize(new Dimension(240, 25));
        splitPaneSidebar.setContinuousLayout(true);
        splitPaneSidebar.setOrientation(JSplitPane.VERTICAL_SPLIT);       
        splitPaneMain.setLeftComponent(splitPaneSidebar);
                
        /* LEFT SIDEBAR TOP CONTAINER
         * --------------------------------------------------------------------------------------------------------- */
        containerLeftSidebarTop = new JPanel();
        containerLeftSidebarTop.setAlignmentY(Component.TOP_ALIGNMENT);
        containerLeftSidebarTop.setAlignmentX(Component.LEFT_ALIGNMENT);
        containerLeftSidebarTop.setLayout(new BoxLayout(containerLeftSidebarTop, BoxLayout.Y_AXIS));
        // splitPaneSidebar.setTopComponent(containerLeftSidebarTop);
        
        /* EXPLORER HEADING CONTAINER
         * --------------------------------------------------------------------------------------------------------- */
        containerExplorerHeading = new JPanel();
        containerExplorerHeading.setPreferredSize(new Dimension(10, 23));
        containerExplorerHeading.setMinimumSize(new Dimension(10, 23));
        containerExplorerHeading.setMaximumSize(new Dimension(32767, 23));
        containerExplorerHeading.setAlignmentY(Component.TOP_ALIGNMENT);
        containerExplorerHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        containerExplorerHeading.setCursor(DEFAULT_CURSOR);
        containerExplorerHeading.setLayout(new BoxLayout(containerExplorerHeading, BoxLayout.Y_AXIS));
        containerExplorerHeading.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 0, 1, (Color) new Color(0, 0, 0)), new EmptyBorder(2, 5, 5, 5)));
        containerLeftSidebarTop.add(containerExplorerHeading);
        
        /* EXPLORER HEADING
         * --------------------------------------------------------------------------------------------------------- */
        labelExplorerHeading = new JLabel("Explorer");
        labelExplorerHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelExplorerHeading.setAlignmentY(Component.TOP_ALIGNMENT);
        labelExplorerHeading.setMaximumSize(new Dimension(32767, 14));
        labelExplorerHeading.setHorizontalTextPosition(SwingConstants.LEFT);
        labelExplorerHeading.setHorizontalAlignment(SwingConstants.LEFT);
        labelExplorerHeading.setFont(new Font("Segoe UI", Font.BOLD, 12));
        containerExplorerHeading.add(labelExplorerHeading);        
        
        /* EXPLORER TREE SCROLLPANE
         * --------------------------------------------------------------------------------------------------------- */
        UIManager.put("ScrollBar.minimumThumbSize", new Dimension(16, 16)); // prevent Windows L&F scroll thumb bug
        scrollPaneExplorer = new JScrollPane(tree);        
        scrollPaneExplorer.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPaneExplorer.setAlignmentY(Component.TOP_ALIGNMENT);
        scrollPaneExplorer.setBorder(new LineBorder(new Color(0, 0, 0)));
        scrollPaneExplorer.setMinimumSize(new Dimension(32767, 200));
        containerLeftSidebarTop.add(scrollPaneExplorer);

        /* TRACK SLIDER
         * --------------------------------------------------------------------------------------------------------- */        
        trackSlider = new TrackSlider();
        trackSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        trackSlider.setPaintTicks(false);
        trackSlider.setPaintLabels(false);
        containerLeftSidebarTop.add(trackSlider);
        
        splitPaneSidebar.setTopComponent(containerLeftSidebarTop);
        
        /* CENTER SPLITPANE
         * (contains search msgPanel) 
         */        
        splitPaneSideCenter.setContinuousLayout(true);
        splitPaneSideCenter.setOrientation(JSplitPane.VERTICAL_SPLIT);       
        splitPaneSideCenter.setOneTouchExpandable(false);
                
        /* SEARCH BY NAME PANEL
         * --------------------------------------------------------------------------------------------------------- */
        searchPanel = new NameSearchPanel(msg);
        searchPanel.setAlignmentX(LEFT_ALIGNMENT);
        searchPanel.setAlignmentY(BOTTOM_ALIGNMENT);
        searchPanel.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 0, 1, (Color) new Color(0, 0, 0)), new EmptyBorder(2, 5, 5, 5)));
        searchPanel.addPropertyChangeListener(propertyListener);
        
        splitPaneSideCenter.setTopComponent(searchPanel);
        splitPaneSideCenter.setResizeWeight(0.1f);

        /* LEFT SIDEBAR BOTTOM CONTAINER
         * --------------------------------------------------------------------------------------------------------- */
        containerLeftSidebarBottom = new JPanel();
        containerLeftSidebarBottom.setAlignmentY(Component.TOP_ALIGNMENT);
        containerLeftSidebarBottom.setAlignmentX(Component.LEFT_ALIGNMENT);
        containerLeftSidebarBottom.setLayout(new BoxLayout(containerLeftSidebarBottom, BoxLayout.Y_AXIS));
        splitPaneSidebar.setBottomComponent(containerLeftSidebarBottom);       
        
        /* PROPERTIES CONTAINER
         * --------------------------------------------------------------------------------------------------------- */
        containerPropertiesHeading = new JPanel();
        containerPropertiesHeading.setMaximumSize(new Dimension(32767, 23));
        containerPropertiesHeading.setMinimumSize(new Dimension(10, 23));
        containerPropertiesHeading.setPreferredSize(new Dimension(10, 23));
        containerPropertiesHeading.setAlignmentY(Component.TOP_ALIGNMENT);
        containerPropertiesHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        containerPropertiesHeading.setCursor(DEFAULT_CURSOR);
        containerPropertiesHeading.setLayout(new BoxLayout(containerPropertiesHeading, BoxLayout.Y_AXIS));
        containerPropertiesHeading.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 0, 1, (Color) new Color(0, 0, 0)), new EmptyBorder(2, 5, 5, 5)));
        containerLeftSidebarBottom.add(containerPropertiesHeading);
        
        /* PROPERTIES HEADING
         * --------------------------------------------------------------------------------------------------------- */
        labelPropertiesHeading = new JLabel("Properties");
        labelPropertiesHeading.setMaximumSize(new Dimension(32767, 14));
        labelPropertiesHeading.setHorizontalTextPosition(SwingConstants.LEFT);
        labelPropertiesHeading.setHorizontalAlignment(SwingConstants.LEFT);
        labelPropertiesHeading.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelPropertiesHeading.setAlignmentY(0.0f);
        containerPropertiesHeading.add(labelPropertiesHeading);
        
        /* PROPERTIES TABLE/MODEL
         * --------------------------------------------------------------------------------------------------------- */        
        propsTableModel = new PropsTableModel(uc);
        tableProperties = new JTable(propsTableModel);
        tableProperties.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tableProperties.setAlignmentY(Component.TOP_ALIGNMENT);
        tableProperties.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableProperties.setBorder(new EmptyBorder(0, 0, 0, 0));
        tableProperties.setGridColor(Color.LIGHT_GRAY);
        tableProperties.setFillsViewportHeight(true);
        tableProperties.setTableHeader(null);
        tableProperties.setEnabled(false);
        tableProperties.getColumnModel().setColumnMargin(0);
        propsTableModel.setTable(tableProperties);        
                
        /* PROPERTIES TABLE SCROLLPANE
         * --------------------------------------------------------------------------------------------------------- */
        scrollPaneProperties = new JScrollPane(tableProperties);
        scrollPaneProperties.setAlignmentY(Component.TOP_ALIGNMENT);
        scrollPaneProperties.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPaneProperties.setBorder(new LineBorder(new Color(0, 0, 0)));
        containerLeftSidebarBottom.add(scrollPaneProperties);

        splitPaneSideCenter.setBottomComponent(containerLeftSidebarBottom);
        splitPaneSidebar.setBottomComponent(splitPaneSideCenter);
        
        /* MAP & CHART PANEL SPLITPANE
         * --------------------------------------------------------------------------------------------------------- */
        splitPaneMap = new JSplitPane();
        splitPaneMap.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPaneMap.setTopComponent(mapPanel);

        splitPaneMain.setRightComponent(splitPaneMap);
        splitPaneMap.setBottomComponent(null);   
        
	}

	/**
	 * 
	 */
	private void setupDownloadBar() {
		
		final String iconPath = Const.ICONPATH_DLBAR;
		
		toolBarDownload = new JToolBar();
		toolBarDownload.setVisible(false);
	
	    /* OPEN FROM DEVICE BUTTON
	     * --------------------------------------------------------------------------------------------------------- */
	    btnDeviceOpen = new JButton("");
	    btnDeviceOpen.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	getFromDevice();
	        }
	    });
	    
	    btnDeviceOpen.setToolTipText("<html>Get Data from Device<br>[CTRL+G]</html>");
	    btnDeviceOpen.setFocusable(false);
	    btnDeviceOpen.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-device.png"))));
	    btnDeviceOpen.setDisabledIcon(
	            new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-device-disabled.png"))));
	    String ctrlDev = "CTRL+G";
	    mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
	            KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK), ctrlDev);
	    mapPanel.getActionMap().put(ctrlDev, new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	getFromDevice(); 
	        }
	    });
	    toolBarDownload.add(btnDeviceOpen);
	    btnDeviceOpen.setEnabled(false); // remove after implementation
	
	    /* DATABASE BUTTON
	     * --------------------------------------------------------------------------------------------------------- */
	    btnDatabase = new JButton("");
	    btnDatabase.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	openDbDialog();
	        }
	    });
	    
	    btnDatabase.setToolTipText("<html>Access GPS Database<br></html>");
	    btnDatabase.setFocusable(false);
	    btnDatabase.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("database.png"))));
	    btnDatabase.setDisabledIcon(
	            new ImageIcon(GpsMaster.class.getResource(iconPath.concat("database-disabled.png"))));
	    toolBarDownload.add(btnDatabase);
	    btnDatabase.setEnabled(false); 
	    btnDatabase.setVisible(false);
	    
	    /* SAVE TO DATABASE BUTTON	     
	     * --------------------------------------------------------------------------------------------------------- */
	    btnDbSave = new JButton();
	    btnDbSave.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	saveToDb();
	        }
	    });

	    btnDbSave.setToolTipText("<html>Save current GPS file to database</html>");
	    btnDbSave.setFocusable(false);
	    btnDbSave.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("db-save.png"))));
	    btnDbSave.setDisabledIcon(
	            new ImageIcon(GpsMaster.class.getResource(iconPath.concat("db-save-disabled.png"))));
	    toolBarDownload.add(btnDbSave);
	    btnDbSave.setEnabled(false);
	    btnDbSave.setVisible(false);
	    
	    /* DOWNLOAD FROM OSM BUTTON
	     * --------------------------------------------------------------------------------------------------------- */
	    btnDownloadOsm = new JButton("");
	    btnDownloadOsm.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	downloadOsm(); 
	        }
	    });
	    
	    btnDownloadOsm.setToolTipText("<html>Download Route from OSM<br>[CTRL+R]</html>");
	    btnDownloadOsm.setFocusable(false);
	    btnDownloadOsm.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("download-osm.png"))));
	    btnDownloadOsm.setDisabledIcon(
	            new ImageIcon(GpsMaster.class.getResource(iconPath.concat("download-osm-disabled.png"))));
	    String ctrlOsm = "CTRL+R";
	    mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
	            KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), ctrlOsm);
	    mapPanel.getActionMap().put(ctrlOsm, new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	downloadOsm(); 
	        }
	    });
	    toolBarDownload.add(btnDownloadOsm);
	    btnDownloadOsm.setEnabled(true);

	    /* DOWNLOAD FROM GPSIES BUTTON
	     * --------------------------------------------------------------------------------------------------------- */
	    btnDownloadGpsies = new JButton("");
	    btnDownloadGpsies.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	downloadGpsies();
	        }
	    });
	    
	    btnDownloadGpsies.setToolTipText("Download Tracks from www.gpsies.com");
	    btnDownloadGpsies.setFocusable(false);
	    btnDownloadGpsies.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("gpsies-down.png"))));
	    btnDownloadGpsies.setDisabledIcon(
	            new ImageIcon(GpsMaster.class.getResource(iconPath.concat("gpsies-down-disabled.png"))));
	    // String ctrlOsm = "CTRL+R";
	    /*
	    mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
	            KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), ctrlOsm);
	    mapPanel.getActionMap().put(ctrlOsm, new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	downloadFromOsm(); 
	        }
	    });
	    */
	    toolBarDownload.add(btnDownloadGpsies);
	    btnDownloadGpsies.setEnabled(true);

	    /* UPLOAD TO GPSIES BUTTON
	     * --------------------------------------------------------------------------------------------------------- */
	    btnUploadGpsies = new JButton("");
	    btnUploadGpsies.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	uploadGpsies();
	        }
	    });
	    
	    btnUploadGpsies.setToolTipText("Upload Track to www.gpsies.com");
	    btnUploadGpsies.setFocusable(false);
	    btnUploadGpsies.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("gpsies-up.png"))));
	    btnUploadGpsies.setDisabledIcon(
	            new ImageIcon(GpsMaster.class.getResource(iconPath.concat("gpsies-up-disabled.png"))));

	    toolBarDownload.add(btnUploadGpsies);
	    btnUploadGpsies.setEnabled(false);

	    /* GET FROM WIKIPEDIA BUTTON
	     * --------------------------------------------------------------------------------------------------------- */
	    btndownloadWiki = new JButton("");
	    btndownloadWiki.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	downloadWiki();
	        }
	    });
	    
	    btndownloadWiki.setToolTipText("<html>Get nearby articles from Wikipedia<br>[CTRL+W]</html>");
	    btndownloadWiki.setFocusable(false);
	    btndownloadWiki.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("wiki-down.png"))));
	    btndownloadWiki.setDisabledIcon(
	            new ImageIcon(GpsMaster.class.getResource(iconPath.concat("wiki-down-disabled.png"))));
	    String ctrlWiki = "CTRL+W";
	    
	    mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
	            KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK), ctrlWiki);
	    mapPanel.getActionMap().put(ctrlOsm, new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	downloadWiki(); 
	        }
	    });
	    
	    toolBarDownload.add(btndownloadWiki);
	    btndownloadWiki.setEnabled(true);

	    setToolbarColor(toolBarDownload, MENU_BACKGROUND);
		menuBarsPanel.add(toolBarDownload, BorderLayout.SOUTH);
	}

	// TODO
	// encapsulate toolbars (menu, download, side) in their own classes
	// with enable/disable methods, possibly consolidate Listeners/Handlers
	// handle "setButtonVisibility" internally, if possible
	// goal: move as many globally defined members/variables into specific classes
	// as possible
	
	
	private void setupCombo() {


	}
	
    /**
     * 
     */
	private void setupMenuBar() {
		
		final String iconPath = Const.ICONPATH_MENUBAR;		
			
		/* MAIN TOOLBAR
         * --------------------------------------------------------------------------------------------------------- */
        toolBarMain = new JToolBar();
        toolBarMain.setLayout(new BoxLayout(toolBarMain, BoxLayout.X_AXIS));
        toolBarMain.setFloatable(false);
        toolBarMain.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));	    
	    
        /* NEW FILE BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        btnFileNew = new JButton("");
        btnFileNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileNew();
            }
        });
        
        btnFileNew.setToolTipText("<html>Create new GPX file<br>[CTRL+N]</html>");
        btnFileNew.setFocusable(false);
        btnFileNew.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-new.png"))));
        btnFileNew.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-new-disabled.png"))));
        String ctrlNew = "CTRL+N";
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), ctrlNew);
        mapPanel.getActionMap().put(ctrlNew, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileNew();
            }
        });        
        toolBarMain.add(btnFileNew);
        
        /* OPEN FILE BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        int chooserWidth = (frame.getWidth() * 8) / 10;
        int chooserHeight = (frame.getHeight() * 8) / 10;
        chooserWidth = Math.min(864, chooserWidth);
        chooserHeight = Math.min(539, chooserHeight);
        
        btnFileOpen = new JButton("");
        chooserFileOpen = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setIconImage(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-open.png"))).getImage());
                return dialog;
            }
        };
        chooserFileOpen.setPreferredSize(new Dimension(chooserWidth, chooserHeight));

        btnFileOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               fileLoad(); // via FileHub
            }
        });
        btnFileOpen.setToolTipText("<html>Open GPX file<br>[CTRL+O]</html>");
        btnFileOpen.setFocusable(false);
        btnFileOpen.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-open.png"))));
        btnFileOpen.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-open-disabled.png"))));
        String ctrlOpen = "CTRL+O";
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), ctrlOpen);
        mapPanel.getActionMap().put(ctrlOpen, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	fileLoad();
            }
        });
        btnFileOpen.setBackground(Color.WHITE);
        toolBarMain.add(btnFileOpen);
        
        /* MORE TRANSFER OPTIONS BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglDownload = new JToggleButton("");
        tglDownload.setToolTipText("More Transfer Options");
        tglDownload.setFocusable(false);
        tglDownload.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-open-more.png"))));
        tglDownload.setEnabled(true);
        toolBarMain.add(tglDownload);
        tglDownload.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    toolBarDownload.setVisible(true);
                    tglDownload.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-open-less.png"))));
                } else {
                	toolBarDownload.setVisible(false);
                	tglDownload.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-open-more.png"))));
                }
            }
        });

        /* SAVE FILE BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        btnFileSave = new JButton("");
        chooserFileSave = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setIconImage(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-save.png"))).getImage());
                return dialog;
            }
        };
        chooserFileSave.setPreferredSize(new Dimension(chooserWidth, chooserHeight));
        btnFileSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileSave();
            }
        });
        
        btnFileSave.setToolTipText("<html>Save selected GPX file<br>[CTRL+S]</html>");
        btnFileSave.setFocusable(false);
        btnFileSave.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-save.png"))));
        btnFileSave.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("file-save-disabled.png"))));
        String ctrlSave = "CTRL+S";
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), ctrlSave);
        mapPanel.getActionMap().put(ctrlSave, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileSave();
            }
        });
        toolBarMain.add(btnFileSave);
        btnFileSave.setEnabled(false);
        
        /* PRINT BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        btnPrint = new JButton("");
        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printVisibleMap();
            }
        });
        
        btnPrint.setToolTipText("<html>Print current map view[CTRL+P]</html>");
        btnPrint.setFocusable(false);
        btnPrint.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("print.png"))));
        btnPrint.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("print-disabled.png"))));
        String ctrlPrint = "CTRL+P";
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), ctrlPrint);
        mapPanel.getActionMap().put(ctrlPrint, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printVisibleMap();
            }
        });
        toolBarMain.add(btnPrint);
        btnPrint.setEnabled(false);
        btnPrint.setVisible(true);

        /* UNDO BUTTON
         * TODO: display list of current undo operations for selection by user
         * --------------------------------------------------------------------------------------------------------- */
        btnUndo = new JButton();
        btnUndo.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();				
			}
		});
        btnUndo.setToolTipText("<html>Undo last operation<br>[CTRL+Z]</html>");
        btnUndo.setFocusable(false);
        btnUndo.setIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("undo.png"))));
        btnUndo.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("undo-disabled.png"))));
        // TODO key handling CTRL-Z on all panels
        String ctrlUndo = "CTRL+Z";
        splitPaneMain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), ctrlUndo);
        splitPaneMain.getActionMap().put(ctrlUndo, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });        
        btnUndo.setEnabled(false);
        btnUndo.setVisible(true);
        toolBarMain.add(btnUndo);
        
        		
        /* OBJECT REMOVE BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        btnObjectDelete = new JButton("");
        btnObjectDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteActiveGPXObject();
            }
        });
        btnObjectDelete.setToolTipText("<html>Delete selected object<br>[CTRL+D]</html>");
        btnObjectDelete.setFocusable(false);
        btnObjectDelete.setIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("object-delete.png"))));
        btnObjectDelete.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("object-delete-disabled.png"))));
        String ctrlDelete = "CTRL+D";
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), ctrlDelete);
        mapPanel.getActionMap().put(ctrlDelete, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteActiveGPXObject();
            }
        });
        toolBarMain.add(btnObjectDelete);
        toolBarMain.addSeparator();
        btnObjectDelete.setEnabled(false);
	        
        /* EDIT PROPERTIES BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        btnEditProperties = new JButton("");
        btnEditProperties.setToolTipText("Edit properties");
        btnEditProperties.setFocusable(false);
        btnEditProperties.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("edit-properties.png"))));
        btnEditProperties.setEnabled(false);
        btnEditProperties.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("edit-properties-disabled.png"))));
        btnEditProperties.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProperties();
            }
        });
        toolBarMain.add(btnEditProperties);
        
        /* PATHFINDER BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglPathFinder = new JToggleButton("");
        tglPathFinder.setToolTipText("Find path");
        tglPathFinder.setFocusable(false);
        tglPathFinder.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("path-find.png"))));
        tglPathFinder.setEnabled(false);
        tglPathFinder.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("path-find-disabled.png"))));
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if (pathFinder != null && active.getGpxObject() != null && !mouseOverLink) {
            		findPath(e);
            	}
            }            
        });

        tglPathFinder.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
            	if (e.getStateChange() == ItemEvent.SELECTED) {
            		deselectAllToggles(tglPathFinder);
            		pathFinderOn();
            	} else {
            		pathFinderOff();
            	}
                frame.revalidate();
                frame.repaint();
            }
        });
        
        toolBarMain.add(tglPathFinder);
        toggles.add(tglPathFinder);
        
        /* ADD POINTS BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglAddRoutepoint = new JToggleButton("");
        tglAddRoutepoint.setToolTipText("Add Route Points");
        tglAddRoutepoint.setFocusable(false);
        tglAddRoutepoint.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("add-points.png"))));
        tglAddRoutepoint.setEnabled(false);
        tglAddRoutepoint.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("add-points-disabled.png"))));
        toolBarMain.add(tglAddRoutepoint);
        toggles.add(tglAddRoutepoint);
        
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	Waypoint wpt = null;
            	GPXObject activeGPXObject = active.getGpxObject();
                if (active.getGpxObject() != null && !mouseOverLink) {
                    int zoom = mapPanel.getZoom();
                    int x = e.getX();
                    int y = e.getY();
                    Point mapCenter = mapPanel.getCenter();
                    int xStart = mapCenter.x - mapPanel.getWidth() / 2;
                    int yStart = mapCenter.y - mapPanel.getHeight() / 2;
                    double lat = OsmMercator.MERCATOR_256.yToLat(yStart + y, zoom);
                    double lon = OsmMercator.MERCATOR_256.xToLon(xStart + x, zoom);
                    
                    GPXFile gpxFile = active.getGpxFile();                    
                	if (tglAddRoutepoint.isSelected()) {
	                    wpt = new Waypoint(lat, lon);
	                    WaypointGroup group = null;
	                    if (activeGPXObject.isGPXFileWithOneRoute()) {
	                        group = ((GPXFile) activeGPXObject).getRoutes().get(0).getPath();
	                    } else if (activeGPXObject.isRoute()) {
	                        group = ((Route) activeGPXObject).getPath();	                        
	                    } else if (activeGPXObject.isWaypointGroup()
	                            && ((WaypointGroup) activeGPXObject).getWptGrpType() == WptGrpType.WAYPOINTS) {
	                        group = (WaypointGroup) activeGPXObject;
	                    }
	                    group.addWaypoint(wpt);
	                    active.addUndoOperation(new UndoAddWaypoint(wpt, group));
	                }
                	if (tglAddWaypoint.isSelected()) {
                		wpt = new WaypointMarker(lat, lon);
                		WaypointGroup group = gpxFile.getWaypointGroup();
                		group.addWaypoint(wpt);
                		active.addUndoOperation(new UndoAddWaypoint(wpt, group));
                		active.refreshTree();                		                		
                	}
                	if (wpt != null) {
	                    gpxFile.updateAllProperties();	                    
	                    active.repaintMap();
	                    active.refresh();
                	}
                }
            }
        });
        tglAddRoutepoint.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    deselectAllToggles(tglAddRoutepoint);
                    mapCursor = CROSSHAIR_CURSOR;
                    GPXObject gpxObject = active.getGpxObject();
                    if (gpxObject.isGPXFileWithNoRoutes()) {                    	
                        Route route = ((GPXFile) gpxObject).addRoute();
                        tree.addGpxObject(route, gpxObject);
                        updateButtonVisibility();
                    }
                } else {
                    mapCursor = DEFAULT_CURSOR;
                }
            }
        });
        
        /* DELETE POINTS BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglDelPoints = new JToggleButton("");
        tglDelPoints.setToolTipText("Delete points");
        tglDelPoints.setFocusable(false);
        tglDelPoints.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("delete-points.png"))));
        tglDelPoints.setEnabled(false);
        tglDelPoints.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("delete-points-disabled.png"))));
        toolBarMain.add(tglDelPoints);
        toggles.add(tglDelPoints);

        tglDelPoints.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    deselectAllToggles(tglDelPoints);
                    mapCursor = CROSSHAIR_CURSOR;
                } else {
                    mapCursor = DEFAULT_CURSOR;
                }
            }
        });
                        
        /* INTERACTIVE CHART BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglChart = new JToggleButton("");
        tglChart.setToolTipText("Show Chart");
        tglChart.setFocusable(false);
        tglChart.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("chart.png"))));
        tglChart.setEnabled(false);
        tglChart.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("chart-disabled.png"))));
        toolBarMain.add(tglChart);
        tglChart.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	enableChart();
                } else {                    
                	disableChart();
                }
            }
        });
                
        /* MEASURE DISTANCE BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        final DirectDistancePainter directPainter = new DirectDistancePainter();
        tglMeasure = new JToggleButton("");
        tglMeasure.setToolTipText("Measure distance between two waypoints");
        tglMeasure.setFocusable(false);
        tglMeasure.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("measure.png"))));
        tglMeasure.setEnabled(false);
        tglMeasure.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("measure-disabled.png"))));
        tglMeasure.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    mapCursor = CROSSHAIR_CURSOR;
                    distanceWidget = new DistanceWidget();
                    mapPanel.add(distanceWidget);
                    measure = new MeasureThings(distanceWidget, uc, mapPanel.getMarkerList());
                    measure.setMessageCenter(msg);
                    mapPanel.addPropertyChangeListener(measure.getPropertyChangeListener());
                    addPropertyChangeListener(measure.getPropertyChangeListener());
                    mapPanel.addPainter(directPainter);
                    // measure.setActiveGpxObject(activeGPXObject);
                } else {
                    mapPanel.removePropertyChangeListener(measure.getPropertyChangeListener());
                    mapPanel.remove(distanceWidget);
                    removePropertyChangeListener(measure.getPropertyChangeListener());
                    mapCursor = DEFAULT_CURSOR;
                    measure.dispose();
                    measure = null;
                    distanceWidget = null;
                    mapPanel.removePainter(directPainter);
                }
            }
        });
        toolBarMain.add(tglMeasure);
        
        // toggles.add(tglMeasure);
        
        /* SHOW PROGRESS LABELS BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglProgress = new JToggleButton("");
        tglProgress.setToolTipText("Show progress labels");
        tglProgress.setFocusable(false);
        tglProgress.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("progress-none.png"))));
        tglProgress.setEnabled(false);
        tglProgress.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("progress-disabled.png"))));
        
        tglProgress.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ((e.getStateChange() == ItemEvent.SELECTED) || (e.getStateChange() == ItemEvent.DESELECTED)) {
                	switch(progressPainter.getProgressType()) {
                	case NONE:
                		progressPainter.setProgressType(ProgressType.RELATIVE);
                		tglProgress.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("progress-rel.png"))));
                		tglProgress.setSelected(true);
                		break;
                	case RELATIVE:
                		progressPainter.setProgressType(ProgressType.ABSOLUTE);
                		tglProgress.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("progress-abs.png"))));
                		tglProgress.setSelected(true);
                		break;
                	case ABSOLUTE:
                		progressPainter.setProgressType(ProgressType.NONE);
                		tglProgress.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("progress-none.png"))));
                		tglProgress.setSelected(false);
                		break;                		
                	}
                }
        		mapPanel.repaint();
            }
        });
        toolBarMain.add(tglProgress);
        
        /* SHOW DIRECTIONAL ARROWS BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglArrows = new JToggleButton("");
        tglArrows.setToolTipText("Show directional arrows [CTRL-A]");
        tglArrows.setFocusable(false);
        tglArrows.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("arrows-enabled.png"))));
        tglArrows.setEnabled(false);
        tglArrows.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("arrows-disabled.png"))));
        toolBarMain.add(tglArrows);
        tglArrows.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ((e.getStateChange() == ItemEvent.SELECTED) || (e.getStateChange() == ItemEvent.DESELECTED)) {
                	switch(arrowPainter.getArrowType()) {
                	case NONE:
                		arrowPainter.setArrowType(ArrowType.ONTRACK);
                		tglArrows.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("arrow-ontrack.png"))));
                		tglArrows.setSelected(true);
                		break;
                	case ONTRACK:
                		arrowPainter.setArrowType(ArrowType.PARALLEL);
                		tglArrows.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("arrow-parallel.png"))));
                		tglArrows.setSelected(true);
                		break;
                	case PARALLEL:
                		arrowPainter.setArrowType(ArrowType.NONE);
                		tglArrows.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("arrows-enabled.png"))));
                		tglArrows.setSelected(false);
                		break;                		
                	}
                }
        		mapPanel.repaint();
            }
        });

        toolBarMain.add(Box.createHorizontalGlue());	

        //
        // the 3 listeners below are shared by multiple functionalities (delete points, split trackseg)
        // --------------------------------------------------------------------------------------------
        
        mapPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            	updateActiveWpt(e);
            }
            @Override // TODO move waypoint
            public void mouseDragged(MouseEvent e) {            
                mapPanel.setShownPoint(null);
                mapPanel.repaint();
            }
        });
        
        mapPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
            	updateActiveWpt(e);
            }
        });
        
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                active.setTrackpoint(null);
                // mapPanel.setShownPoint(null);
                // mapPanel.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            	Waypoint activeWpt = active.getTrackpoint();
                if ((activeWpt != null) && (mouseOverLink == false)) {
                    GPXFile gpxFile = active.getGpxFile();
                    
                    // notify listeners of the newly selected Waypoint
                    firePropertyChange("1click", null, activeWpt); // activeWpt for legacy, pass NULL later
                    
                    // legacy: handle selected waypoint the old way
                    if (tglDelPoints.isSelected()) {
                    	WaypointGroup group = active.getGroup(); 
                    	if (group != null) {
                    		active.addUndoOperation(new UndoRemoveWaypoint(activeWpt, group));
                    		group.removeWaypoint(activeWpt);
                    		gpxFile.updateAllProperties();
                    	}
                    } else if (tglSplitTrackseg.isSelected()) {               	                   
                        splitTrackSeg(gpxFile);
                    } 
                    
                    mapPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	updateActiveWpt(e); // necessary?
            }
        });
        
        /* TOGGLE TOOLBAR BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglToolbar = new JToggleButton("");
        tglToolbar.setToolTipText("Display Toolbar");
        tglToolbar.setFocusable(false);
        tglToolbar.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("toolbar-enabled.png"))));
        tglToolbar.setEnabled(true);
        tglToolbar.setDisabledIcon(
                new ImageIcon(GpsMaster.class.getResource(iconPath.concat("arrows-disabled.png")))); // TODO
        toolBarMain.add(tglToolbar);
        tglToolbar.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
            	switch(e.getStateChange()) {
            	case ItemEvent.SELECTED:
            		contentPane.add(toolBarSide, BorderLayout.WEST);	            		
            		break;
            	case ItemEvent.DESELECTED:
            		contentPane.remove(toolBarSide);	            		
            		break;
            	}
            	frame.validate();
            }
        });
        
        /* TILE SOURCE SELECTOR
         * --------------------------------------------------------------------------------------------------------- */
        final TileSource openStreetMap = new OsmTileSource.Mapnik();
        final TileSource openCycleMap = new OsmTileSource.CycleMap(); 
        final TileSource bingAerial = new BingAerialTileSource();
        final TileSource mapQuestOsm = new MapQuestOsmTileSource();
        final TileSource mapQuestOpenAerial = new MapQuestOpenAerialTileSource();
                     
        /*
        final TileSource openSeaMap = new TemplatedTMSTileSource(
                "OpenSeaMap",
                "http://tiles.openseamap.org/seamark/{z}/{x}/{y}.png", "openseamap", 18);                
        final TileSource stamenToner = new TemplatedTMSTileSource(
                "Stamen Toner",
                "http://tile.stamen.com/toner/{zoom}/{x}/{y}.png", "toner", 18);        
        */
        // final TileSource stamenToner = new TemplatedTMSTileSource(new TileSourceInfo("Stamen Toner", "http://tile.stamen.com/toner/{zoom}/{x}/{y}.png", "toner"));
        
        comboBoxTileSource = new JComboBox<TileSource>();
        comboBoxTileSource.setMaximumRowCount(18);       
        comboBoxTileSource.addItem(new OsmTileSource.Mapnik());
        comboBoxTileSource.addItem(new OsmTileSource.CycleMap());
        comboBoxTileSource.addItem(new BingAerialTileSource());
        comboBoxTileSource.addItem(new MapQuestOsmTileSource());
        comboBoxTileSource.addItem(new MapQuestOpenAerialTileSource());  
        // comboBoxTileSource.addItem("OpenSeaMap");
        // comboBoxTileSource.addItem(stamenToner);
        
        for (OnlineTileSource tileSource : conf.getOnlineTileSources()) {
        	final TileSourceInfo info = new TileSourceInfo(tileSource.getName(), tileSource.getUrl(), Integer.toHexString(tileSource.hashCode()));
        	comboBoxTileSource.addItem(new TemplatedTMSTileSource(info));
        }
        
        comboBoxTileSource.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapPanel.setTileSource((TileSource) comboBoxTileSource.getSelectedItem());                    
            }
        });
        
        comboBoxTileSource.setFocusable(false);
        toolBarMain.add(comboBoxTileSource);
        
        // the tile sources below are not licensed for public usage
        
        /*final TileSource googleMaps = new TemplatedTMSTileSource(
                "Google Maps",
                "http://mt{switch:0,1,2,3}.google.com/vt/lyrs=m&x={x}&y={y}&z={zoom}", 22);
        final TileSource googleSat = new TemplatedTMSTileSource(
                "Google Satellite",
                "http://mt{switch:0,1,2,3}.google.com/vt/lyrs=s&x={x}&y={y}&z={zoom}", 21);
        final TileSource googleSatMap = new TemplatedTMSTileSource(
                "Google Satellite + Labels",
                "http://mt{switch:0,1,2,3}.google.com/vt/lyrs=y&x={x}&y={y}&z={zoom}", 21);
        final TileSource googleTerrain = new TemplatedTMSTileSource(
                "Google Terrain",
                "http://mt{switch:0,1,2,3}.google.com/vt/lyrs=p&x={x}&y={y}&z={zoom}", 15);
        final TileSource esriTopoUSA = new TemplatedTMSTileSource(
                "Esri Topo USA",
                "http://server.arcgisonline.com/ArcGIS/rest/services/" +
                "USA_Topo_Maps/MapServer/tile/{zoom}/{y}/{x}.jpg", 15);
        final TileSource esriTopoWorld = new TemplatedTMSTileSource(
                "Esri Topo World",
                "http://server.arcgisonline.com/ArcGIS/rest/services/" +
                "World_Topo_Map/MapServer/tile/{zoom}/{y}/{x}.jpg", 19);

        comboBoxTileSource.addItem("Google Maps");
        comboBoxTileSource.addItem("Google Satellite");
        comboBoxTileSource.addItem("Google Satellite + Labels");
        comboBoxTileSource.addItem("Google Terrain");
        comboBoxTileSource.addItem("Esri Topo USA");
        comboBoxTileSource.addItem("Esri Topo World");
        
        comboBoxTileSource.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) comboBoxTileSource.getSelectedItem();
                if (selected.equals("Google Maps")) {
                    mapPanel.setTileSource(googleMaps);
                } else if (selected.equals("Google Satellite")) {
                    mapPanel.setTileSource(googleSat);
                } else if (selected.equals("Google Satellite + Labels")) {
                    mapPanel.setTileSource(googleSatMap);
                } else if (selected.equals("Google Terrain")) {
                    mapPanel.setTileSource(googleTerrain);
                } else if (selected.equals("Esri Topo USA")) {
                    mapPanel.setTileSource(esriTopoUSA);
                } else if (selected.equals("Esri Topo World")) {
                    mapPanel.setTileSource(esriTopoWorld);
                }
            }
        });*/
        
        comboBoxTileSource.setMaximumSize(comboBoxTileSource.getPreferredSize());       
        
        /* LAT/LON INPUT/SEEKER
         * --------------------------------------------------------------------------------------------------------- */
        toolBarMain.addSeparator();
        
        lblLat = new JLabel(" Lat ");
        lblLat.setFont(new Font("Tahoma", Font.PLAIN, 11));
        toolBarMain.add(lblLat);
        
        textFieldLat = new JTextField();
        textFieldLat.setPreferredSize(new Dimension(80, 24));
        textFieldLat.setMinimumSize(new Dimension(25, 24));
        textFieldLat.setMaximumSize(new Dimension(80, 24));
        textFieldLat.setColumns(9);
        textFieldLat.setFocusable(false);
        textFieldLat.setFocusTraversalKeysEnabled(false);
        textFieldLat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    textFieldLat.setFocusable(false);
                    textFieldLon.setFocusable(true);
                    textFieldLon.requestFocusInWindow();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    tglLatLonFocus.setSelected(false);
                    tglLatLonFocus.setSelected(true);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tglLatLonFocus.setSelected(false);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (tglLatLonFocus.isSelected()) {
                    tglLatLonFocus.setSelected(false);
                    tglLatLonFocus.setSelected(true);
                }
            }
        });
        toolBarMain.add(textFieldLat);
        
        lblLon = new JLabel(" Lon ");
        lblLon.setFont(new Font("Tahoma", Font.PLAIN, 11));
        toolBarMain.add(lblLon);
        
        textFieldLon = new JTextField();
        textFieldLon.setPreferredSize(new Dimension(80, 24));
        textFieldLon.setMinimumSize(new Dimension(25, 24));
        textFieldLon.setMaximumSize(new Dimension(80, 24));
        textFieldLon.setColumns(9);
        textFieldLon.setFocusable(false);
        textFieldLon.setFocusTraversalKeysEnabled(false);
        textFieldLon.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    textFieldLat.setFocusable(true);
                    textFieldLon.setFocusable(false);
                    textFieldLat.requestFocusInWindow();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    tglLatLonFocus.setSelected(false);
                    tglLatLonFocus.setSelected(true);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tglLatLonFocus.setSelected(false);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (tglLatLonFocus.isSelected()) {
                    tglLatLonFocus.setSelected(false);
                    tglLatLonFocus.setSelected(true);
                }
            }
        });
        toolBarMain.add(textFieldLon);
        
        long eventMask = AWTEvent.MOUSE_EVENT_MASK;  
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {  
            public void eventDispatched(AWTEvent e) {
                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                    if (e.getSource() == (Object) textFieldLat) {
                        textFieldLat.setFocusable(true);
                    } else {
                        textFieldLat.setFocusable(false);
                    }
                    if (e.getSource() == (Object) textFieldLon) {
                        textFieldLon.setFocusable(true);
                    } else {
                        textFieldLon.setFocusable(false);
                    }
                }
            }
        }, eventMask);
        
        tglLatLonFocus = new JToggleButton("");
        tglLatLonFocus.setToolTipText("Focus on latitude/longitude");
        tglLatLonFocus.setIcon(new ImageIcon(GpsMaster.class.getResource(iconPath.concat("crosshair.png"))));
        tglLatLonFocus.setFocusable(false);
        tglLatLonFocus.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    deselectAllToggles(tglLatLonFocus);
                    mapCursor = CROSSHAIR_CURSOR;
                    String latString = textFieldLat.getText();
                    String lonString = textFieldLon.getText();
                    try {                    	 
                        double latDouble = Double.parseDouble(latString);
                        double lonDouble = Double.parseDouble(lonString);                        
                        mapPanel.setShowCrosshair(true);
                        mapPanel.setCrosshairLat(latDouble);
                        mapPanel.setCrosshairLon(lonDouble);
                        Point p = new Point(mapPanel.getWidth() / 2, mapPanel.getHeight() / 2); 
                        mapPanel.setDisplayPosition(p, new Coordinate(latDouble, lonDouble), mapPanel.getZoom()); 
                    } catch (Exception e1) {
                        // nothing
                    }
                    mapPanel.repaint();
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    mapCursor = DEFAULT_CURSOR;
                    mapPanel.setShowCrosshair(false);
                    mapPanel.repaint();
                }
            }
        });

        Component horizontalGlue = Box.createHorizontalGlue();
        horizontalGlue.setMaximumSize(new Dimension(2, 0));
        horizontalGlue.setMinimumSize(new Dimension(2, 0));
        horizontalGlue.setPreferredSize(new Dimension(2, 0));
        toolBarMain.add(horizontalGlue);
        toolBarMain.add(tglLatLonFocus);
        toggles.add(tglLatLonFocus);

        /* AUTOFIT BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglAutoFit = new JToggleButton();
        tglAutoFit.setToolTipText("Resize map msgPanel to fit selected track or segment");
        tglAutoFit.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("autofit.png"))));        
        tglAutoFit.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("autofit-disabled.png"))));
        tglAutoFit.setEnabled(autoFitToPanel);
        tglAutoFit.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				autoFitToPanel = ((e.getStateChange() == ItemEvent.SELECTED));								
			}
		});
        tglAutoFit.setSelected(autoFitToPanel);
        tglAutoFit.setEnabled(true);
        toolBarMain.add(tglAutoFit);

        /* INFO BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        btnInfo = new JButton("");
        btnInfo.setToolTipText("Show Info");
        btnInfo.setFocusable(false);
        btnInfo.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("about.png"))));
        btnInfo.setEnabled(true);
        btnInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInfo();
            }
        });
        toolBarMain.add(btnInfo);

        // EndRegion      
        
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tglLatLonFocus.isSelected() && !mouseOverLink) {
                    int zoom = mapPanel.getZoom();
                    int x = e.getX();
                    int y = e.getY();
                    Point mapCenter = mapPanel.getCenter();
                    int xStart = mapCenter.x - mapPanel.getWidth() / 2;
                    int yStart = mapCenter.y - mapPanel.getHeight() / 2;
                    double lat = OsmMercator.MERCATOR_256.yToLat(yStart + y, zoom);
                    double lon = OsmMercator.MERCATOR_256.xToLon(xStart + x, zoom);
                    textFieldLat.setText(String.format("%.6f", lat));
                    textFieldLon.setText(String.format("%.6f", lon));
                    mapPanel.setShowCrosshair(true);
                    mapPanel.setCrosshairLat(lat);
                    mapPanel.setCrosshairLon(lon);
                    mapPanel.repaint();
                }
            }
        });

        // set up file save/open dialogs
		for (String ext : loaderFactory.getExtensions()) {
			String desc = ext.toUpperCase() + " files (*." + ext+ ")";
			FileNameExtensionFilter extFilter = new FileNameExtensionFilter(desc, ext);
		    chooserFileOpen.addChoosableFileFilter(extFilter);
		    chooserFileSave.addChoosableFileFilter(extFilter);
		    if (ext.equals(conf.getDefaultExt())) { 
		    	chooserFileOpen.setFileFilter(extFilter);
		    }
		}

        /*
         * save config on exit
         */
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	        
        frame.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent we) {
        		saveConfig();
        		if (db != null) {
        			try {
						db.disconnect();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        		System.exit(0);
        	}
        });
                                 
        /* DEBUG
         * --------------------------------------------------------------------------------------------------------- */
        
        // button for quick easy debugging
        JButton debug = new JButton("debug");
        debug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doDebug();
            }
        });
        debug.setFocusable(false);
        // toolBarMain.addSeparator();
        // toolBarMain.add(debug);
        
        setToolbarColor(toolBarMain, MENU_BACKGROUND);
		menuBarsPanel.add(toolBarMain, BorderLayout.NORTH);
		       
	}
	
	/**
	 * 
	 */
	private void setupToolbar() {
				
		final String iconPath = Const.ICONPATH_TOOLBAR;
		
		/* FLOATABLE TOOLBAR
	     * --------------------------------------------------------------------------------------------------------- */
	    toolBarSide = new JToolBar(JToolBar.VERTICAL);
	    toolBarSide.setFloatable(true);
	    toolBarSide.setName("Toolbar");
	    
	    // --- EDITING -----------------------------
	    
        /* SPLIT TRACKSEG BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglSplitTrackseg = new JToggleButton("");
        tglSplitTrackseg.setToolTipText("Split track segment");
        tglSplitTrackseg.setFocusable(false);
        tglSplitTrackseg.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("trackseg-split.png"))));
        tglSplitTrackseg.setEnabled(false);
        tglSplitTrackseg.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("trackseg-split-disabled.png"))));
        toolBarSide.add(tglSplitTrackseg);
        toggles.add(tglSplitTrackseg);

        tglSplitTrackseg.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    deselectAllToggles(tglSplitTrackseg);
                    mapCursor = CROSSHAIR_CURSOR;
                } else {
                    mapCursor = DEFAULT_CURSOR;
                }
            }
        });
        
        /* ADD WAYPOINT BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        tglAddWaypoint = new JToggleButton("");
        tglAddWaypoint.setToolTipText("Add new Waypoint");
        tglAddWaypoint.setFocusable(false);
        tglAddWaypoint.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("add-waypoint.png"))));
        tglAddWaypoint.setEnabled(false);
        tglAddWaypoint.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("add-waypoint-disabled.png"))));
        toolBarSide.add(tglAddWaypoint);
        toggles.add(tglAddWaypoint);

        tglAddWaypoint.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    deselectAllToggles(tglAddWaypoint);
                    mapCursor = CROSSHAIR_CURSOR;
                } else {
                    mapCursor = DEFAULT_CURSOR;
                }
            }
        });
        
        toolBarSide.addSeparator();
        
        // --- CLEANING & CORRECTION -----------------------------
        
        
        /* CORRECT ELEVATION BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        btnCorrectEle = new JButton("");
        btnCorrectEle.setToolTipText("Correct elevation");
        btnCorrectEle.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("correct-elevation.png"))));
        btnCorrectEle.setEnabled(false);
        btnCorrectEle.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("correct-elevation-disabled.png"))));
        btnCorrectEle.setFocusable(false);
        btnCorrectEle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               	correctElevation();
            }
        });
        toolBarSide.add(btnCorrectEle);

        /* CLEAN NARROW WAYPOINTS
         * --------------------------------------------------------------------------------------------------------- */
        btnCleaning = new JButton("");
        btnCleaning.setToolTipText(String.format("Remove waypoints"));
        btnCleaning.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("clean-distance.png"))));
        btnCleaning.setEnabled(false);
        btnCleaning.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("clean-distance-disabled.png"))));
        btnCleaning.setFocusable(false);
        btnCleaning.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	doCleaning();
            }
        });
        toolBarSide.add(btnCleaning);


        /* TIMESHIFT BUTTON
         * --------------------------------------------------------------------------------------------------------- */
        btnTimeShift = new JButton("");
        btnTimeShift.setToolTipText("modify GPX timestamps [CTRL-T]");
        btnTimeShift.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("timeshift.png"))));
        btnTimeShift.setEnabled(false);
        btnTimeShift.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("timeshift-disabled.png"))));
        btnTimeShift.setFocusable(false);
        btnTimeShift.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTimeShift();
            }
        });
        String ctrlT = "CTRL+T";
        mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK), ctrlT);
        mapPanel.getActionMap().put(ctrlT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTimeShift();
            }
        });
        toolBarSide.add(btnTimeShift);
        
        toolBarSide.addSeparator();
        
        /* MERGE 1:1 INTO NEW GPX
         * --------------------------------------------------------------------------------------------------------- */
        btnMergeOneToOne = new JButton("");
        btnMergeOneToOne.setToolTipText("Merge visible tracks and segments 1:1 into a new GPX file.");
        btnMergeOneToOne.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("merge-tracks.png"))));
        btnMergeOneToOne.setEnabled(false);
        btnMergeOneToOne.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("merge-tracks-disabled.png")))); 
        btnMergeOneToOne.setFocusable(false);
        btnMergeOneToOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// TODO ask for confirmation
            	GPXFile newFile = core.mergeIntoTracks(mapPanel.getGPXFiles());
            	if (newFile != null) {            		
            		// firePropertyChange(Const.PCE_NEWGPX, null, newFile);
            		GpsMaster.active.newGpxFile(newFile);
            	}
               	msg.volatileInfo("Merging completed.");
            }
        });
        toolBarSide.add(btnMergeOneToOne);
        
        /* MERGE INTO SINGLE TRACK, MULTIPLE SEGMENTS
         * --------------------------------------------------------------------------------------------------------- */
        btnMergeToMulti = new JButton("");
        btnMergeToMulti.setToolTipText("Merge visible tracks and segments into a single track, keep segments separate.");
        btnMergeToMulti.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("merge-multi.png"))));
        btnMergeToMulti.setEnabled(false);
        btnMergeToMulti.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("merge-multi-disabled.png"))));
        btnMergeToMulti.setFocusable(false);
        btnMergeToMulti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// TODO ask for confirmation
            	GPXFile newFile = core.mergeIntoMulti(mapPanel.getGPXFiles());
            	if (newFile != null) {
            		// firePropertyChange(Const.PCE_NEWGPX, null, newFile);
            		GpsMaster.active.newGpxFile(newFile);
            	}
               	msg.volatileInfo("Merging completed.");
            }
        });
        toolBarSide.add(btnMergeToMulti);

        /* MERGE INTO SINGLE TRACK, SINGLE SEGMENT
         * --------------------------------------------------------------------------------------------------------- */        
        btnMergeToSingle = new JButton("");
        btnMergeToSingle.setToolTipText("Merge visible track segments into a new track with a single segment and sort trackpoints by time.");
        btnMergeToSingle.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("merge-single.png"))));
        btnMergeToSingle.setEnabled(false);
        btnMergeToSingle.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("merge-single-disabled.png"))));
        btnMergeToSingle.setFocusable(false);
        btnMergeToSingle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// TODO ask for confirmation
            	GPXFile newFile = core.mergeIntoSingle(mapPanel.getGPXFiles());
            	if (newFile != null) {            	
            		GpsMaster.active.newGpxFile(newFile);
            	}
               	msg.volatileInfo("Merging completed.");
            }
        });
        toolBarSide.add(btnMergeToSingle);

        /* MERGE PARALLEL SEGMENTS
         * --------------------------------------------------------------------------------------------------------- */        
        btnMergeParallel = new JButton("");
        btnMergeParallel.setToolTipText("Interpolate a new track between two track segments.");
        btnMergeParallel.setIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("merge-parallel.png"))));
        btnMergeParallel.setEnabled(false);
        btnMergeParallel.setDisabledIcon(new ImageIcon(
                GpsMaster.class.getResource(iconPath.concat("merge-parallel-disabled.png"))));
        btnMergeParallel.setFocusable(false);
        btnMergeParallel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// TODO ask for confirmation
               	msg.volatileInfo("Merging completed.");
            }
        });
        // toolBarSide.add(btnMergeParallel);
        
        // toolBarSide.addSeparator();
        
        setToolbarColor(toolBarSide, MENU_BACKGROUND);
	}

	
	/**
	 * Setup database (if configured)
	 */
	private void setupDatabase() {
		
		if (conf.getDbConfig().getDSN().isEmpty() == false) {			
			db = new DbLayer(conf.getDbConfig());
			try {
				db.connect();
				btnDatabase.setVisible(true);
				btnDbSave.setVisible(true);
			} catch (SQLException e) {
				msg.error("Connection to database failed, check configuration", e);
				db = null;
			}			
		}
		
		btnDatabase.setEnabled(db != null);
		btnDbSave.setEnabled(db != null);
	}
	
	/**
	 * set the background color of the menuBar and all components
	 * to the given color
	 * 
	 * @param toolBar
	 * @param color
	 */
	private void setToolbarColor(JToolBar toolBar, Color color) {
		
		toolBar.setBackground(color);
		for (Component comp : toolBar.getComponents()) {
			comp.setBackground(color);
		}
		
	}
	
	/**
	 * Dynamically enables/disables certain toolbar buttons dependent on which type of {@link GPXObject} is active
	 * and what operations are allowed on that type of element.
	 * TODO rewrite, consolidate code
	 */
	private void updateButtonVisibility() {

		// unsorted
		btnFileSave.setEnabled(false);
	    btnObjectDelete.setEnabled(false);
	    tglAddRoutepoint.setEnabled(false);
	    tglDelPoints.setEnabled(false);
	    tglSplitTrackseg.setEnabled(false);
	    btnEditProperties.setEnabled(false);
	    tglPathFinder.setEnabled(false);
	    tglMeasure.setEnabled(false);
	    tglProgress.setEnabled(false);
	    tglArrows.setEnabled(false);
	    btnTimeShift.setEnabled(false);
	    btnPrint.setEnabled(false);
	    
	    // menuBar
	    tglToolbar.setEnabled(true);	// always on
	    tglDownload.setEnabled(true);	// always on
	    btnFileNew.setEnabled(true);	// always on
	    btnFileOpen.setEnabled(true);	// always on
	    tglChart.setEnabled(false);
	    
	    // downloadBar
	    // btnDownloadOsm.setEnabled(true);	// always on
	    // btnDownloadGpsies.setEnabled(true);	// always on
	    btndownloadWiki.setEnabled(true);
	    btnDbSave.setEnabled(false);
	    
		// ToolBar
	    btnMergeOneToOne.setEnabled(false);
	    btnMergeToSingle.setEnabled(false);
	    btnMergeToMulti.setEnabled(false);
	    btnMergeParallel.setEnabled(false);
	    btnCleaning.setEnabled(false);
	    btnCorrectEle.setEnabled(false);
	    
	    btnTimeShift.setEnabled(true); // temp.
	    
	    GPXObject o = active.getGpxObject();
	    
	    if (o != null) {
	        btnFileSave.setEnabled(true);
	        btnObjectDelete.setEnabled(true);
	        btnPrint.setEnabled(true);
	        tglChart.setEnabled(true);
	        tglMeasure.setEnabled(true);
	        btnDbSave.setEnabled(true);
	        if (db != null) {
	        	btnDbSave.setEnabled(true);
	        }
	        if (o.isRoute() || o.isWaypoints() || o.isGPXFileWithOneRoute() || o.isGPXFileWithNoRoutes()) {
	            tglAddRoutepoint.setEnabled(true);
	        }
	        if (o.isTrackseg() || o.isTrackWithOneSeg() || o.isRoute() || o.isWaypoints()
	                || o.isGPXFileWithOneTracksegOnly() || o.isGPXFileWithOneRouteOnly()) {
	            tglDelPoints.setEnabled(true);
	        }
	        if (o.isTrackseg() || o .isTrackWithOneSeg() || o.isGPXFileWithOneTracksegOnly()){
	            tglSplitTrackseg.setEnabled(true);
	        }
	        if (o.isTrack() || o.isRoute() || o.isTrackseg()) {
	            tglMeasure.setEnabled(true);
	        }
	        if (o.isGPXFile() || o.isRoute() || o.isTrack()) {
	            btnEditProperties.setEnabled(true);
	            btnCorrectEle.setEnabled(true);
	            tglSplitTrackseg.setEnabled(true);
	            btnCleaning.setEnabled(true);
	        }
	        if (o.isRoute() || o.isGPXFileWithOneRoute() || o.isGPXFileWithNoRoutes()) {
	            tglPathFinder.setEnabled(true);
	        }
	        if (o.isTrackseg() || o.isTrack() || o.isGPXFile()) {
	            tglProgress.setEnabled(true);
	            tglArrows.setEnabled(true);
	            btnCorrectEle.setEnabled(true);
	            btnUploadGpsies.setEnabled(true);
	            // btnTimeShift.setEnabled(true); // as soon as "shift by delta" is implemented
	        }
	        if (o.isTrackseg()) {
	        	// btnTimeShift.setEnabled(true);	        	
	        }
	        
	        if (mapPanel.getGPXFiles().size() > 0) {
	        	btnMergeOneToOne.setEnabled(true);
	        	btnMergeToMulti.setEnabled(true);
	        	btnMergeToSingle.setEnabled(true);
	        	// btnMergeParallel.setEnabled(true);
	        	tglAddWaypoint.setEnabled(true);
	        }
	    }
	    
	    btnUndo.setEnabled(active.getUndoStack().size() > 0);
	    
	}
	
	/**
     * 
     * @param gpxFile
     * 
     * TODO bug: {@link NullPointerException} after repeated splits
     */
	private void splitTrackSeg(GPXFile gpxFile) {
		WaypointGroup tracksegBeforeSplit = active.getGroup();
		
		List<Waypoint> trackptsBeforeSplit = tracksegBeforeSplit.getWaypoints();
		int splitIndex = trackptsBeforeSplit.indexOf(active.getTrackpoint());
		
		List<Waypoint> trackptsAfterSplit1 = new ArrayList<Waypoint>(
		        trackptsBeforeSplit.subList(0, splitIndex + 1));
		List<Waypoint> trackptsAfterSplit2 = new ArrayList<Waypoint>(
		        trackptsBeforeSplit.subList(splitIndex, trackptsBeforeSplit.size()));
		WaypointGroup tracksegAfterSplit1 =
		        new WaypointGroup(tracksegBeforeSplit.getColor(), WptGrpType.TRACKSEG);
		WaypointGroup tracksegAfterSplit2 =
		        new WaypointGroup(tracksegBeforeSplit.getColor(), WptGrpType.TRACKSEG);
		tracksegAfterSplit1.setWaypoints(trackptsAfterSplit1);
		tracksegAfterSplit2.setWaypoints(trackptsAfterSplit2);
		
		Track track = active.getTrackForSegment(tracksegBeforeSplit);
		int insertIndex = track.getTracksegs().indexOf(tracksegBeforeSplit);
		track.getTracksegs().remove(tracksegBeforeSplit);
		track.getTracksegs().add(insertIndex, tracksegAfterSplit2);
		track.getTracksegs().add(insertIndex, tracksegAfterSplit1);

		gpxFile.updateAllProperties();
		active.addUndoOperation(new UndoSplitTrackSeg(track, tracksegAfterSplit1, tracksegAfterSplit1));
		active.refreshTree();
		active.refresh();
		tglSplitTrackseg.setSelected(true);
		active.setTrackpoint(null);
	}

    /**
     * Creates a new GPX file and loads it into the application.
     */
	private void fileNew() {
        
        String name = (String)JOptionPane.showInputDialog(frame, "Please type a name for the new route:",
                "New route", JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (name != null) {
            GPXFile gpxFile = new GPXFile(name);
            gpxFile.getMetadata().setTime(new Date());
            gpxFile.addRoute();
            gpxFile.updateAllProperties();
            mapPanel.addGPXFile(gpxFile);
            tree.addGpxFile(gpxFile);            
        }
    }
	
    /**
	 * Saves the active {@link GPXFile} to disk.
	 */
	private void fileSave() {
		
		GPXFile gpx = active.getGpxFile();
		
	    if (gpx == null) {
	        return;
	    }
	    	    
        SwingWorker<Void, Void> fileSaveWorker = new SwingWorker<Void, Void>() {
        	
        	MessagePanel msgSave = null;
            @Override
            public Void doInBackground() {
				GpsLoader loader;				
		        setFileIOHappening(true);
		        msgSave = msg.infoOn("Saving file ...", WAIT_CURSOR);
				try {
					fileSave = chooserFileSave.getSelectedFile();
					loader = GpsLoaderFactory.getLoaderByExtension(getFilenameExt(fileSave.getName()));
					loader.save(active.getGpxFile(), fileSave);
				} catch (ClassNotFoundException e) {
					msg.error("No writer for this file format available.");
				} catch (FileNotFoundException e) {
					msg.error(e);
				} catch (Exception e) {
					msg.error("unexpected error", e);
				}                
                return null;
            }
            @Override
            protected void done() {
            	msg.infoOff(msgSave);
                setFileIOHappening(false);
            }
        };

	    chooserFileSave.setCurrentDirectory(
                new File(conf.getLastSaveDirectory()));

	    boolean hasKnownExt = false;
	    String filename = gpx.getMetadata().getName();
	    for (String ext : loaderFactory.getExtensions()) {
	    	if (filename.endsWith(ext)) {
	    		hasKnownExt = true;
	    	}
	    }
	    if (!hasKnownExt) { filename = filename.concat(".gpx"); }
        chooserFileSave.setSelectedFile(new File(filename));

	    boolean ok = false;
	    while(!ok) {
	    	ok = true;
		    int returnVal = chooserFileSave.showSaveDialog(frame);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		        fileSave = chooserFileSave.getSelectedFile();
		        String ext = getFilenameExt(fileSave.getName());
		        if (ok && ext.isEmpty()) {
		        	JOptionPane.showConfirmDialog(frame, "please specify an sourceFmt.",
		                    "no sourceFmt", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
		        	ok = false;
		        }  
		        if (ok && !loaderFactory.getExtensions().contains(ext)) {
		        	JOptionPane.showConfirmDialog(frame, "unsupported file format.",
		                    "error", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
		        	ok = false;
		        }
		       
		        if (ok && fileSave.exists()) {
		            int response = JOptionPane.showConfirmDialog(frame, "<html>" + fileSave.getName() +
		                    " already exists.<br>Do you want to replace it?</html>",
		                    "Confirm file overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		            if (response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION) {
		                return; // cancel the save operation
		            }
		        }
		        conf.setLastSaveDirectory(chooserFileSave.getCurrentDirectory().getPath());
		        fileSaveWorker.execute();
		    }
	    }	    		      
	}

	/**
     * 
     * @param filename
     * @return
     */
    private String getFilenameExt(String fileName) {
    	String extension = "";

    	int i = fileName.lastIndexOf('.');
    	int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

    	if (i > p) {
    	    extension = fileName.substring(i+1);
    	}
    	return extension;
    }    
            
    /**
     * Removes the active {@link GPXObject} from its parent container.
     * TODO create & push Undo Operations
     */
    private void deleteActiveGPXObject() {

        GPXObject gpxObject = active.getGpxObject();
        GPXFile gpxFile = active.getGpxFile();
        
        if (gpxObject != null) {
        	// tree.removeGpxObject(gpxObject);                        
            if (gpxObject.isGPXFile()) { // this is a GPX file
            	mapPanel.removeGPXFile(gpxFile); // TODO remove in msgPanel via active.propertyHandler
            	tree.removeGpxFile(gpxFile);
            } else {
                if (gpxObject.isRoute()) { // this is a route
                    gpxFile.getRoutes().remove((Route) gpxObject);
                    // TODO remove RoutePointMarker
                } else if (gpxObject.isTrack()) { // this is a track
                	gpxFile.getTracks().remove((Track) gpxObject);
                } else if (gpxObject.isWaypointGroup()) {
                    WaypointGroup wptGrp = (WaypointGroup) gpxObject; 
                    if (wptGrp.getWptGrpType() == WptGrpType.TRACKSEG) { // track seg
                    	for (Track track : gpxFile.getTracks()) {
                    		if (track.getTracksegs().contains(wptGrp)) {
                    			track.getTracksegs().remove(wptGrp);
                    			break;
                    		}
                    	}
                    } else { // this is a top-level waypoint group
                        gpxFile.getWaypointGroup().getWaypoints().clear();
                    }
                }                
                gpxFile.updateAllProperties();                
            } 
            active.refreshTree();
            mapPanel.repaint();            
            active.setGpxObject(null);  // TODO tree.current = parent object, also set active.gpxobject to parent
        }
    }

    /**
     * Convert active route to track
     */
    private void routeToTrack() {
    	
    	if (!(active.getGpxObject() instanceof Route)) {
    		msg.volatileError("not a route");
    	}
    	Route route = (Route) active.getGpxObject();
    	GPXFile gpx = active.getGpxFile();
    	
    	Track track = new Track(route.getColor());    	
    	track.setName(route.getName().replace("Route", "Track"));
    	track.setCmt(route.getCmt());
    	track.setDesc(route.getDesc());
    	track.setSrc(route.getSrc());    	
    	track.addTrackseg(new WaypointGroup(route.getPath()));
    	gpx.addTrack(track);
    	gpx.updateAllProperties();
    	active.refresh();
    	active.refreshTree();
    }
    
    /**
     * Convert active track to route
     */
    private void trackToRoute() {
    	
    	if (!(active.getGpxObject() instanceof Track)) {
    		msg.volatileError("not a track");
    	}
    	
    	Track track = (Track) active.getGpxObject();
    	if (track.getTracksegs().size() > 1) {
    		msg.volatileError("Track contains more than one segment");
    	}
    	
    	GPXFile gpx = active.getGpxFile();
    	Route route = gpx.addRoute();
    	route.setName(track.getName().replace("Track", "Route"));
    	route.setCmt(track.getCmt());
    	route.setDesc(track.getDesc());
    	route.setSrc(track.getSrc());    
    	for (Waypoint wpt : track.getTracksegs().get(0).getWaypoints()) {
    		route.getPath().addWaypoint(new Waypoint(wpt));    		
    	}    	
    	gpx.updateAllProperties();
    	active.refresh();
    	active.refreshTree();        
    }
    
    /**
     * add a new route to the active GPX file
     */
    private void addRoute() {
    	
    	GPXFile gpx = active.getGpxFile();
    	Route newRoute = gpx.addRoute();
    	newRoute.setName("New Route");
    	active.getUndoStack().push(new UndoAddRoute(newRoute, gpx));
    	active.refresh();
    	active.refreshTree();
    }
    
    /**
     * perform undo operation
     */
    private void undo() {
    	if (active.getUndoStack().size() > 0) {
    		try {
	    		IUndoable undo = active.getUndoStack().pop();
	    		undo.undo();
	    		undo = null;
    		} catch (Exception e) {
    			msg.error("Undo failed", e);
    		}
    	}
    	firePropertyChange(Const.PCE_UNDO, null, null);
    	active.refresh();
    	active.repaintMap();
    }
    
    /**
     * invoked when an operation has been pushed to 
     * or popped from the undo stack
     */
    private void handleUndoNotification() {
    	if (active.getUndoStack().size() > 0) {
    		IUndoable undo = active.getUndoStack().peek();
    		btnUndo.setToolTipText("Undo: " + undo.getUndoDescription() + " (CTRL-Z)");    		
    	}
    	updateButtonVisibility();
    }
    
    /**
     * Handler to re-enable menu buttons when corresponding window is closed
     * @param e
     * TODO implement this as hashtable <Dialog, Button>
     */
    private void handleWindowEvent(WindowEvent e, int state) {
    	
    	Object o = e.getSource(); 
    	if ((state == WindowEvent.WINDOW_CLOSED) || (state == WindowEvent.WINDOW_CLOSING)){
    		if (o instanceof DownloadGpsies) {
    			btnDownloadGpsies.setEnabled(true);    			
    		}
    		if (o instanceof UploadGpsies) {
    			btnUploadGpsies.setEnabled(true);    			
    		}
    		if (o instanceof DownloadOsm) {
    			btnDownloadOsm.setEnabled(true);    			
    		}
    		if (o instanceof GetWikipedia) {
    			btndownloadWiki.setEnabled(true);    			
    		}
    		if (o instanceof InfoDialog) {
    			btnInfo.setEnabled(true);    			
    		}
    		if (o instanceof CleaningDialog) {
    			btnCleaning.setEnabled(true);    			
    		} 
    		if (o instanceof ChartWindow) {
    			tglChart.setSelected(false);    			
    		} 
    		if (o instanceof ImageViewer) {
    			if (imageViewer != null) {    				
    				imageViewer = null;
    			}
    		}
    		if (o instanceof DBDialog) {
    			btnDatabase.setEnabled(true);
    		}
    		if (o instanceof TimeshiftDialog) {
    			btnTimeShift.setEnabled(true);    			
    		} 

    	}
    }
    
    /**
     * thread-safe (?) method to modify GPXObjects in the GPXFiles list
     * and synchronize with tree model.
     * called via PropertyChangeHandlers by user dialogs
     */
    private void handlePropertyChangeEvent(PropertyChangeEvent event) {
    	String command = event.getPropertyName();
    	// System.out.println(" ** " + command);    	    		
		if (command.equals(Const.PCE_NEWGPX)) {
			// add a new GPXFile
			handleNewGpx(event);
		} else if (command.equals(Const.PCE_ACTIVEGPX)) {
	    	if (autoFitToPanel) {
	    		mapPanel.fitGPXObjectToPanel(active.getGpxObject());
	    	}
	    	// to be handled by the toolbars themselves in the future:
	    	updateButtonVisibility();	    	
		} else if (command.equals(Const.PCE_REMOVEGPX)) {
			deleteActiveGPXObject();
		} else if (command.equals(Const.PCE_TOTRACK)) { // convert route to track
			routeToTrack();
		} else if (command.equals(Const.PCE_TOROUTE)) { // convert track to route
			trackToRoute();
		} else if (command.equals(Const.PCE_ADDROUTE)) { // add a new route
			addRoute();
		} else if (command.equals(Const.PCE_ADDROUTEPT)) { // add a new point to route
			// TODO besser SearchPanel & PathFinder direkt vernetzen
			if (pathFinder != null) {
				Waypoint wpt = (Waypoint) event.getNewValue(); 
				findPath(wpt);
			}
		} else if (command.equals("1click")) {
			handle1Click(event.getNewValue());
		} else if (command.equals("2click")) {
			handle2Click(event.getNewValue());
		} else if (command.equals("gpsiesUsername")) {
			conf.setGpsiesUsername((String) event.getNewValue());
		} else if (command.equals(Const.PCE_UNDO)) {
			handleUndoNotification();
		}    	
    }
    
    /**
     * handle the arrival of a new {@link GPXFile}
     * @param event
     */
    private void handleNewGpx(PropertyChangeEvent event) {
		GPXFile gpxFile = (GPXFile) event.getNewValue();
		
    	gpxFile.updateAllProperties();
        // adjustColors(gpxFile); // make this configurable?
    	if (conf.useExtensions()) {
    		postLoad(gpxFile);
    	}
    	mapPanel.addGPXFile(gpxFile); // mapPanel.addGPXFile is thread safe
        tree.addGpxFile(gpxFile);
        active.setGpxObject(gpxFile);       
        mapPanel.setShownPoint(null);        
        
    }
    
    /**
     * handle single click on a marker object
     * @param o
     */
    private void handle1Click(Object o) {    	
    	if ((o instanceof PhotoMarker) && (imageViewer != null)) {
    		imageViewer.showMarker((PhotoMarker) o);    		
    	} else if ((o instanceof Marker) && tglDelPoints.isSelected()) {
    		// System.out.println("1click marker");
    		mapPanel.getMarkerList().remove(o);
    		GPXFile gpx = active.getGpxFile();
    		if (gpx != null) {
    			gpx.getWaypointGroup().removeWaypoint((Marker) o);
    			mapPanel.repaint();
    		}
    	}
    }

    /**
     * handle double click on a marker object
     * @param o
     */
    private void handle2Click(Object o) {
    	if (o instanceof WikiMarker) {
        	// wikipedia: open link to article
    		WikiMarker marker = (WikiMarker) o; 
    		if (marker.getLink().size() > 0) {
    			BrowserLauncher.launchBrowser(marker.getLink().get(0).getHref());
    		}
    	} else if (o instanceof PhotoMarker) {
    		// photo: open imageviewer
    		if (imageViewer == null) {
    			imageViewer = new ImageViewer(frame, msg);    			
    			imageViewer.setAlwaysOnTop(true);
    			imageViewer.addWindowListener(windowListener);
    			imageViewer.setGpxFiles(mapPanel.getGPXFiles());
    			imageViewer.begin();    			
    		}
			imageViewer.showMarker((PhotoMarker) o);
    	} else if (o instanceof WaypointMarker) {
    		for (LinkType link : ((WaypointMarker) o).getLink()) {
    			BrowserLauncher.launchBrowser(link.getHref());
    		}
    	}
    }
    
    /**
     * Check if there is a trackpoint ({@link Waypoint}) under the mouse cursor
     * If yes, highlight it and set it as active {@link Waypoint}
     * @param e
     */
    private void updateActiveWpt(MouseEvent e) {
    	if (active.getGpxObject() != null) { // TODO check visibility!
    		Waypoint activeWpt = null;    		    	
    		Point p = e.getPoint(); // screen coordinates
    		// TODO check somehow if e.coordinate is within min/max lat/lon
    		boolean found = false;
    		for (WaypointGroup group : active.getGroups()) {    			
    			if (group.isVisible()) { // TODO bug: check if parent objects are visible
    									// (implement better visibility handling design)
	                int start = 0;
	                int end = group.getNumPts();
	                double minDistance = Double.MAX_VALUE;
	                if (tglSplitTrackseg.isSelected()) { // don't allow splitting at endpoints
	                    start = 1;
	                    end = group.getNumPts() - 1;
	                }
	                for (int i = start; i < end; i++) {
	                    Waypoint wpt = group.getWaypoints().get(i);
	                    Point w = mapPanel.getMapPosition(wpt.getLat(), wpt.getLon(), false);
	                    int dx = w.x - p.x;
	                    int dy = w.y - p.y;
	                    double distance = Math.sqrt(dx * dx + dy * dy);
	                    if ((distance < 10) && (distance < minDistance)) { // found
	                        minDistance = distance;
	                        activeWpt = wpt;
	                        found = true;
	                    }
	                }
    			}
    			if (found) {
    				active.setGroup(group);
    				active.setTrackpoint(activeWpt, false); // no need to autoSetGroup since we already set it
    				break;
    			}
    		}
    		if (found == false) {
        		active.setGroup(null);        		
        		active.setTrackpoint(null);
        		mapPanel.setShownPoint(null);
    		}
    	}
    }
        
    /**
     * Registers a list of {@link JToggleButton}s and deselects them all, optionally leaving one selected.
     * Used to prevent multiple toggles from being selected simultaneously.
     */
    private void deselectAllToggles(JToggleButton exceptThisOne) {
                
        for (JToggleButton toggle : toggles) {
            if (toggle != exceptThisOne && toggle.isSelected()) {
                toggle.setSelected(false);
            }
        }        
    }
    
    /**
     * Initalize all objects required for pathfinding operation
     */
    private void pathFinderOn() {

    	pathFinder = new PathFinder();
    	pathFinder.setGpxObject();
    	pathFinder.setMessageCenter(msg);
    	pathFinderWidget = new PathFinderWidget(pathFinder);    	
    	pathFinderWidget.setRouteProviders(RouteProviderFactory.getAllProviders());

    	searchPanel.setRoutepointEnabled(true); 
    	mapPanel.add(pathFinderWidget);
    	
        mapCursor = CROSSHAIR_CURSOR; // does not work
        routeInfoPanel = msg.infoOn("Click on map to add points along the planned route");
    }
    
    /**
     * 
     */
    private void pathFinderOff() {

    	msg.infoOff(routeInfoPanel);
        mapCursor = DEFAULT_CURSOR;
        searchPanel.setRoutepointEnabled(false);
    	if (pathFinderWidget != null) {
    		mapPanel.remove(pathFinderWidget);
    		pathFinderWidget = null;
    	}
    	pathFinder.clear();
    	pathFinder = null;
    }
    
    /**
	 * Find a path / route on click on map
	 *  
	 * @param e
	 */
	private void findPath(MouseEvent e) {
	
    	int zoom = mapPanel.getZoom();
        int x = e.getX();
        int y = e.getY();
        Point mapCenter = mapPanel.getCenter();
        int xStart = mapCenter.x - mapPanel.getWidth() / 2;
        int yStart = mapCenter.y - mapPanel.getHeight() / 2;
        final double lat = OsmMercator.MERCATOR_256.yToLat(yStart + y, zoom);
        final double lon = OsmMercator.MERCATOR_256.xToLon(xStart + x, zoom);
        
        findPath(new Waypoint(lat, lon));
	}

	/**
	 * 
	 * @param wpt
	 */
	private void findPath(Waypoint wpt) {
		try {
			pathFinder.findRoute(wpt);
		} catch (Exception e) {
			msg.error(e);			
		}
	}

	/**
     * Initialise and assign all objects for displaying the interactive chart
     */
    private void enableChart() {    	
    	chartWindow = new ChartWindow(frame);
    	chartWindow.addWindowListener(windowListener);
    	chartWindow.setVisible(false);
        chartHandler = new ChartHandler(uc);
        chartHandler.setParentPane(splitPaneMap, mapToChartRatio);
        splitPaneMap.setResizeWeight(mapToChartRatio);
        chartHandler.setChartWindow(chartWindow);
        active.addPropertyChangeListener(chartHandler.getPropertyChangeListener());
        chartHandler.setActiveGpxObject(active.getGpxObject());       
        chartHandler.setInteractive(true);
    }
    
	/**
     * disable interactive chart & clean up related objects
     */
    private void disableChart() {
        chartHandler.setParentPane(null, 0.0);    	
        active.removePropertyChangeListener(chartHandler.getPropertyChangeListener());
        chartHandler.terminate();
        chartHandler = null;
        chartWindow = null;    	
    }
    
    
    /**
	 * 
	 */
	private void preload() {
		
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				ChartHandler handler = new ChartHandler(uc);
				ChartWindow window = new ChartWindow(frame);
				return null;
			}    		
		};
		worker.execute();
	}

	/**
     * Displays the edit properties dialog and saves the user-selected values to the active {@link GPXObject}.
     * TODO rewrite 
     */
    private void editProperties() {
    	GPXObject activeGPXObject = active.getGpxObject();
        EditPropsDialog dlg = new EditPropsDialog(frame, "Edit properties", activeGPXObject);
        dlg.setVisible(true);
         if (activeGPXObject.isGPXFile()) {
        	GPXFile gpx = (GPXFile) activeGPXObject;
        	if (gpx.getExtension().containsKey(Const.EXT_ACTIVITY)) {
        		gpx.getExtension().remove(Const.EXT_ACTIVITY);        		
        	}        	
        	if (dlg.getActivity().isEmpty() == false) {
	        	gpx.getExtension().add(Const.EXT_ACTIVITY, dlg.getActivity());
	        	if (activityHandler != null) {
	        		activityHandler.setActivity(dlg.getActivity());
	        	}
	        }
	        if (dlg.getName() != null) {
	            gpx.getMetadata().setName(dlg.getName());
	        }
	        if (dlg.getDesc() != null) {
	            gpx.getMetadata().setDesc(dlg.getDesc());
	        }
        } else {
            if (dlg.getName() != null) {
                activeGPXObject.setName(dlg.getName());
            }
            if (dlg.getDesc() != null) {
                activeGPXObject.setDesc(dlg.getDesc());
            }
        }
        
        if (activeGPXObject.isRoute()) {
            if (dlg.getGPXType() != null) {
                ((Route) activeGPXObject).setType(dlg.getGPXType());
            }
            if (dlg.getNumber() != null) {
                ((Route) activeGPXObject).setNumber(dlg.getNumber());
            }
        }
        if (activeGPXObject.isTrack()) {
            if (dlg.getGPXType() != null) {
                ((Track) activeGPXObject).setType(dlg.getGPXType());
            }
            if (dlg.getNumber() != null) {
                ((Track) activeGPXObject).setNumber(dlg.getNumber());
            }
        }

    }


    /**
     * apply color modifications to a {@link GPXFile}
     * use a different color for each track, if there
     * is more than one track.
     */
    private void adjustColors(GPXFile gpxFile) {
    	
    	for (Track track: gpxFile.getTracks()) {
    		// TODO: how to color tracks
    		
	    	// if a track contains multiple segments, 
	    	// color them in different shades           
    		if (track.getTracksegs().size() > 1) {
    			int delta = 25;
    			int red = track.getColor().getRed();
    			int green = track.getColor().getGreen();
    			int blue = track.getColor().getBlue();
    		    // increase every "Black" channel by delta 
    			// decrease every "White" channel by delta
    			for (WaypointGroup seg: track.getTracksegs()) {    				
    				if (seg.getExtension().containsKey(Const.EXT_COLOR) == false) {
	    			    if (track.getColor().getRed() == 0)
	    			    {
	    			    	red = Math.min(red + delta, 255);	
	    			    } else {
	    			    	red = Math.max(red - delta, 0);
	    			    }
	    			    if (track.getColor().getGreen() == 0)
	    			    {
	    			    	green = Math.min(green + delta, 255);	
	    			    } else {
	    			    	green = Math.max(green - delta, 0);
	    			    }
	    			    if (track.getColor().getBlue() == 0)
	    			    {
	    			    	blue = Math.min(blue + delta, 255);	
	    			    } else {
	    			    	blue = Math.max(blue - delta, 0);
	    			    }
	    				 
	    			    Color newColor = new Color(red, green, blue);  		
	    				seg.setColor(newColor);
    				}
    			}
    		}
    	}
    }

    /**
	 * Set up the given {@link FileHub} as follows:
	 * 
	 * - show progress widget on {@link GPXPanel} during transfers (visible on/off)
	 * - show transfer status in {@link MessageCenter} when finished
	 * 
	 * @param fileHub the {@link FileHub} to set up
	 * @param title {@link ProgressWidget} title
	 */
	private void setupFileHub(final FileHub fileHub, String title) {
	
		final ProgressWidget progressWidget = new ProgressWidget();
	    progressWidget.setTitle(title);
	    
		PropertyChangeListener changeListener = new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
	
				String propertyName = evt.getPropertyName();
			
				if (propertyName.equals(Const.PCE_TRANSFERSTARTED)) {
					mapPanel.add(progressWidget);
					mapPanel.validate();
				} else if (propertyName.equals(Const.PCE_TRANSFERFINISHED)) {
					mapPanel.remove(progressWidget);    
					if (fileHub.isCancelled()) {
						msg.volatileWarning("Transfer of files cancelled");
					}
					fileHub.getItemSource().getItems().clear();
					showItemTransferStatus(fileHub.getProcessedItems());
					fileHub.getProcessedItems().clear(); // concurrency!											
				}				
			}
		};
	
	    fileHub.setProgressReporter(progressWidget);
	    fileHub.addChangeListener(changeListener);        
	}

    /**
	 * Loads GPS file(s) from filesystem via central {@link FileHub}
	 * TODO unify with {@link GenericDownloadDialog}
	 */
	private void fileLoad() {
	    
		chooserFileOpen.setCurrentDirectory(new File(conf.getLastOpenDirectory()));
		chooserFileOpen.setMultiSelectionEnabled(true);		
	    int returnVal = chooserFileOpen.showOpenDialog(frame);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	conf.setLastOpenDirectory(chooserFileOpen.getCurrentDirectory().getPath());
	        for (File file : chooserFileOpen.getSelectedFiles()) {
	        	centralFileHub.addItem(new FileItem(file));	        	
	        }
	        centralFileHub.run();
	    }
	}

		
	/**
	 * display summary of {@link FileHub} transfer task on {@link MessageCenter}
	 * @param items
	 * TODO directly couple {@link FileHub} and {@link MessageCenter} via event handler
	 */
	private void showItemTransferStatus(List<TransferableItem> items) {
		
		int ok = 0;
		int warning = 0;
		int failed = 0;
		    	
		for (TransferableItem item : items) {
			if (item.getFailureState() == TransferLogEntry.INFO) {
				ok++;
			}
			if (item.getFailureState() == TransferLogEntry.WARNING) {
				warning++;
			}
			if (item.getFailureState() == TransferLogEntry.ERROR) {
				failed++;
			}
		}
		
		if (ok > 0) {
			msg.volatileInfo(String.format("%d file(s) loaded successfully.", ok));
		}
		if (warning > 0) {
			msg.volatileWarning(String.format("%d file(s) loaded with warnings.", warning));
		}
		if (failed > 0) {
			msg.error(String.format("Transfer of %d file(s) failed.", failed));
		}
	}

	/**
     * Set up & show Gpsies download dialog
     */
    private void downloadGpsies() {
    	FileHub fileHub = new FileHub();
    	setupFileHub(fileHub, "Downloading from Gpsies.com");    	
		btnDownloadGpsies.setEnabled(false);
		GenericDownloadDialog gpsies = new DownloadGpsies(frame, msg, fileHub, uc);
		fileHub.setItemSource(gpsies);
		fileHub.addItemTarget(transferTargetMap);
        
		/*
		FileTarget test = new FileTarget();
        test.setDirectory("d:\\temp\\");
        fileHub.addItemTarget(test);    
        DbTarget dbTarget = new DbTarget(db);
        dbTarget.setEnabled(true);
        fileHub.addItemTarget(dbTarget);
        */
		/*
		mapPanel.addJMVListener(new JMapViewerEventListener() {
			
			@Override
			public void processCommand(JMVCommandEvent command) {
				System.out.println(command.getCommand());
				
			}
		});
		*/
		gpsies.setGeoBounds(mapPanel.getVisibleBounds());
		gpsies.addWindowListener(windowListener);
		gpsies.begin();    	        	
    }
    
    private void uploadGpsies() {
		btnUploadGpsies.setEnabled(false);
		UploadGpsies gpsies = new UploadGpsies(active.getGpxFile(), frame, msg);
		if (conf.getGpsiesUsername() != null) {
			gpsies.setUsername(conf.getGpsiesUsername()); 
		}			
		gpsies.addWindowListener(windowListener);
		gpsies.begin();
	}
    
    /**
     * 
     */
    private void downloadOsm() {
    	FileHub fileHub = new FileHub();
    	setupFileHub(fileHub, "Downloading from OpenStreetMap");    	
		btnDownloadOsm.setEnabled(false);
		GenericDownloadDialog osmDialog = new DownloadOsm(frame, msg, fileHub, uc);
		fileHub.setItemSource(osmDialog);
		fileHub.addItemTarget(transferTargetMap);
				
		osmDialog.setGeoBounds(mapPanel.getVisibleBounds());		
		osmDialog.addWindowListener(windowListener);
		osmDialog.begin();    	
    }
    
	/**
	 * 
	 */
	private void downloadWiki() {
    	FileHub fileHub = new FileHub();
    	setupFileHub(fileHub, "Downloading from Wikipedia");   
		btndownloadWiki.setEnabled(false);
		GenericDownloadDialog getWiki = new GetWikipedia(frame, msg, fileHub, uc);
		fileHub.setItemSource(getWiki);
		fileHub.addItemTarget(transferTargetMap);
		getWiki.setGeoBounds(mapPanel.getVisibleBounds());
		getWiki.addWindowListener(windowListener);
		getWiki.begin();  
	}

	/**
	 * 
	 */
	private void openDbDialog() {
		btnDatabase.setEnabled(false);
		FileHub fileHub = new FileHub();
		setupFileHub(fileHub, "Getting from Database");
		DBDialog dbDialog = new DBDialog(frame, msg, db, fileHub);
		fileHub.setItemSource(dbDialog);
		fileHub.addItemTarget(transferTargetMap);
				
		dbDialog.setUnitConverter(uc);		
		addPropertyChangeListener(dbDialog.getChangeListener());
		dbDialog.addWindowListener(windowListener);
		dbDialog.begin();  
	}
	
	/**
	 * Store active {@link GPXFile} to database
	 */
	private void saveToDb() {
						
		final FileHub fileHub = new FileHub();
		setupFileHub(fileHub, "Save to Database");
		fileHub.setItemSource(new MapSource(tree));
		DbTarget dbTarget = new DbTarget(db);
		dbTarget.setEnabled(true);
		fileHub.addItemTarget(dbTarget);
		fileHub.run();		
	}
	
	/**
     * 
     */
    private void showInfo() {
    	// TODO rewrite info dialog
    	btnInfo.setEnabled(false);
    	InfoDialog dlg = new InfoDialog(frame, ME);
    	dlg.addWindowListener(windowListener);
    	dlg.begin();
    	dlg.setVisible(true);    	
    }
    
    /**
     * open timeshift dialog
     */
    private void doTimeShift() {
    	btnTimeShift.setEnabled(false);
		TimeshiftDialog dlg = new TimeshiftDialog(frame, msg);			
		dlg.addPropertyChangeListener(propertyListener);
		dlg.addWindowListener(windowListener);
		dlg.begin();	
    }
    
    /**
     * Open track cleaning dialog
     */
    private void doCleaning() {
    	btnCleaning.setEnabled(false);
		CleaningDialog dlg = new CleaningDialog(frame, msg);
		dlg.setMarkerList(mapPanel.getMarkerList());			
		dlg.addPropertyChangeListener(propertyListener);
		dlg.addWindowListener(windowListener);
		dlg.begin();		 
    }

    /**
     * check for legacy attributes
     * TODO rework
     * @param o
     */
    private void checkLegacy(GPXObject o) {
    	final String OLD_ACT = "activity";
    	
    	Color color = null;
    	String colorString = null;
    	GPXExtension ext = null;
    	
    	if (o.getExtension().containsKey("color")) {
    		colorString = o.getExtension().getSubValue("color");
    		o.getExtension().remove("color");
    		try {	    		
	    		String[] rgba = colorString.split(","); // old format r,g,b,a
	    		if (rgba.length == 4) {    			
    				int r = Integer.parseInt(rgba[0]);
    				int g = Integer.parseInt(rgba[1]);
    				int b = Integer.parseInt(rgba[2]);
    				int a = Integer.parseInt(rgba[3]);
    				color = new Color(r, g, b, a);	    		
	    		}
			} catch (NumberFormatException e) {
				msg.volatileWarning("Unsupported color format", e);
			}    			    			
    	}
    	if (color != null) {
    		o.setColor(color);
    	} // else show warning?
    	
    	// gpx sourceFmt: activity -> gpsm:activity
    	ext = o.getExtension().getExtension(OLD_ACT);
    	if (ext != null) {
    		ext.setValue(Const.EXT_ACTIVITY);
    	}
    }
    
    /**
     * post-process {@link GPXFile} after loading
     * (set GpsMaster specific values according to extensions)
     */
    private void postLoad(GPXFile gpx) {
    	if (gpx.getMetadata().getTime() == null) {
			// try to set date to date of first waypoint
			if (gpx.getTracks().size() > 0) {
				Track track = gpx.getTracks().get(0);
				if (track.getTracksegs().size() > 0) {
					gpx.getMetadata().setTime(track.getTracksegs().get(0).getStart().getTime());
				}   					   					
			}
    	}
    	for (Track track : gpx.getTracks()) {
    		Collections.sort(track.getTracksegs());
    	}
    	Collections.sort(gpx.getTracks());
    	Collections.sort(gpx.getWaypointGroup().getWaypoints());
    }
        
    /**
     * print the visible contents of the map msgPanel 
     */
    private void printVisibleMap() {
    	
    	PrinterJob job = PrinterJob.getPrinterJob();
    	job.setJobName(ME);
    	    	
    	if (job.printDialog()) {
    		MapPrinter mapPrinter = new MapPrinter();
    		mapPrinter.setMapPanel(mapPanel);
    		job.setPrintable(mapPrinter);
    		try {
    			job.print();
    		} catch (PrinterException e) {
    			msg.error(e);
    		}
    	}    		    
    }
    
    /**
     * 
     */
    private void saveMapToImage() {
    	
    	int width = mapPanel.getWidth();
    	int height = mapPanel.getHeight();
    	BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	mapPanel.paint(img.getGraphics());
    	try {
			ImageIO.write(img, "png", new File("mapPanel.png"));
		} catch (IOException e) {
			msg.error(e);
		}
    	
    }
    
    /*
     * Load configuration from file
     */
    private void loadConfig() {
       	try {
       		FileInputStream inputStream = new FileInputStream(configFilename);
    		JAXBContext context = JAXBContext.newInstance(Config.class);
    		Unmarshaller u = context.createUnmarshaller();
    		conf = (Config) u.unmarshal(inputStream);
    		inputStream.close();
    	} catch (FileNotFoundException e) {            
            msg.volatileWarning("Configuration file not found, using defaults.");
    	} catch (JAXBException e) {
    		// JAXBException.getMessage() == null !!
    		msg.volatileWarning("Unable to parse configuration file, using defaults. existing file will be overwritten on exit.");
		} catch (IOException e) {
			msg.error(e);
		} finally {
			if (conf == null) {
				conf = new Config();
			}
		}
       	// set some additional defaults, if applicable       	
       	if (conf.getPalette().size() == 0) {
       		initColors(conf.getPalette());
       	}       	
    	
       	// tmp
        /*final TileSource googleMaps = new TemplatedTMSTileSource(
        "Google Maps",
        "http://mt{switch:0,1,2,3}.google.com/vt/lyrs=m&x={x}&y={y}&z={zoom}", 22);
final TileSource googleSat = new TemplatedTMSTileSource(
        "Google Satellite",
        "http://mt{switch:0,1,2,3}.google.com/vt/lyrs=s&x={x}&y={y}&z={zoom}", 21);
final TileSource googleSatMap = new TemplatedTMSTileSource(
        "Google Satellite + Labels",
        "http://mt{switch:0,1,2,3}.google.com/vt/lyrs=y&x={x}&y={y}&z={zoom}", 21);
final TileSource googleTerrain = new TemplatedTMSTileSource(
        "Google Terrain",
        "http://mt{switch:0,1,2,3}.google.com/vt/lyrs=p&x={x}&y={y}&z={zoom}", 15);
final TileSource esriTopoUSA = new TemplatedTMSTileSource(
        "Esri Topo USA",
        "http://server.arcgisonline.com/ArcGIS/rest/services/" +
        "USA_Topo_Maps/MapServer/tile/{zoom}/{y}/{x}.jpg", 15);
final TileSource esriTopoWorld = new TemplatedTMSTileSource(
        "Esri Topo World",
        "http://server.arcgisonline.com/ArcGIS/rest/services/" +
        "World_Topo_Map/MapServer/tile/{zoom}/{y}/{x}.jpg", 19);

       	OnlineTileSource ts = new OnlineTileSource();
       	ts.setName("Stamen Toner");
       	ts.setUrl("http://tile.stamen.com/toner/{zoom}/{x}/{y}.png");
       	conf.getOnlineTileSources().add(ts);
     
      	ts = new OnlineTileSource();
       	ts.setName("Google Maps");
       	ts.setUrl("http://mt{switch:0,1,2,3}.google.com/vt/lyrs=m&x={x}&y={y}&z={zoom}");
       	conf.getOnlineTileSources().add(ts);
       	*/
       	
    }
    
    /*
     * save current configuration to file
     */
    private void saveConfig() {
    	
        Point mapCenter = mapPanel.getCenter();
        double lat = OsmMercator.MERCATOR_256.yToLat(mapCenter.y, mapPanel.getZoom());
        double lon = OsmMercator.MERCATOR_256.xToLon(mapCenter.x, mapPanel.getZoom());     
        conf.setLon(lon);
        conf.setLat(lat);
        conf.setPositionZoom(mapPanel.getZoom());
        
        // temp
        // conf.setShowWarning(true);
        // conf.setUseExtensions(false);
/*
        DeviceConfig deviceConfig = new DeviceConfig();
        deviceConfig.setName("MoveBikeComputer@Galaxy S3");
        deviceConfig.setDescription("get Tracks recorded with Move!BikeComputer on GT-I9300 via MTP");
        deviceConfig.setLoaderClass("org.gpsmaster.device.MoveBikeCompMPT");
        deviceConfig.getConnectionParams().put("DBFILE", "Internal storage/bikeComputer/database/bikecomp.db");
        deviceConfig.getConnectionParams().put("DEVICE", "GT-I9300");
        conf.getDeviceLoaders().add(deviceConfig);
*/
        
    	try {    		
			FileOutputStream outStream = new FileOutputStream(configFilename);
			
			JAXBContext context = JAXBContext.newInstance(Config.class);
			Marshaller m = context.createMarshaller();
			
			m.marshal(conf, outStream);
			outStream.flush();
			outStream.close();
			} catch (Exception e) {   		
				msg.volatileWarning("Error saving configuration", e);           
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ie) {
            }
    	}
    }

    /**
     * Create list of default colors
     * TODO redundant with GPXObject.colors[]. consolidate.
     */
    private void initColors(List<NamedColor> l) {
    	    	
    	// RAL signal colors
    	l.add(new NamedColor(new Color(0xF7, 0xBA, 0x0B), "RAL 1003 Signal Yellow")); 
    	l.add(new NamedColor(new Color(0xD4, 0x65, 0x2F), "RAL 2010 Signal Orange"));
    	l.add(new NamedColor(new Color(0xA0, 0x21, 0x28), "RAL 3001 Signal Red"));
    	l.add(new NamedColor(new Color(0x90, 0x46, 0x84), "RAL 4008 Signal Violet"));
    	l.add(new NamedColor(new Color(0x15, 0x48, 0x89), "RAL 5005 Signal Blue"));
    	l.add(new NamedColor(new Color(0x0F, 0x85, 0x58), "RAL 6032 Signal Green"));
    	l.add(new NamedColor(new Color(0x9E, 0xA0, 0xA1), "RAL 7004 Signal Grey"));
    	l.add(new NamedColor(new Color(0x7B, 0x51, 0x41), "RAL 8002 Signal Brown"));
    	l.add(new NamedColor(new Color(0xF4, 0xF8, 0xF4), "RAL 9003 Signal White"));
      	// RAL Traffic Colors 
    	l.add(new NamedColor(new Color(0xF0, 0xCA, 0x00), "RAL 1023 Traffic Yellow"));  
    	l.add(new NamedColor(new Color(0xE1, 0x55, 0x01), "RAL 2009 Traffic Orange"));
    	l.add(new NamedColor(new Color(0xC1, 0x12, 0x1C), "RAL 3020 Traffic Red"));
    	l.add(new NamedColor(new Color(0x99, 0x25, 0x72), "RAL 4006 Traffic Purple"));
    	l.add(new NamedColor(new Color(0x0E, 0x51, 0x8D), "RAL 5017 Traffic Blue"));
    	l.add(new NamedColor(new Color(0x00, 0x87, 0x54), "RAL 6024 Traffic Green"));
    	l.add(new NamedColor(new Color(0x8F, 0x96, 0x95), "RAL 7042 Traffic Grey A"));
    	l.add(new NamedColor(new Color(0x4E, 0x54, 0x51), "RAL 7043 Traffic Grey B"));
    	l.add(new NamedColor(new Color(0xF7, 0xFB, 0xF5), "RAL 9016 Traffic White"));
    }
    
    /**
     * open and run elevation correction dialog
     */
    private void correctElevation() {
		btnCorrectEle.setEnabled(false);		

		PropertyChangeListener changeListener = new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(Const.PCE_ELEFINISHED)) {
					// re-enable menu buttons    			    		
					btnCorrectEle.setEnabled(true);
					mapPanel.remove((ProgressWidget) eleCorr.getProgressReporter());    
					if (eleCorr.isCancelled()) {
						msg.volatileWarning("Elevation correction cancelled");
					} else {
						msg.volatileInfo("Elevation correction finished");
					}
				}				
			}
		};
		ProgressWidget progressWidget = new ProgressWidget();		
		ElevationProvider provider = new MapQuestProvider();
		progressWidget.setFooter(provider.getAttribution());
		eleCorr = new Corrector(provider);
		eleCorr.setProgressReporter(progressWidget);
		eleCorr.setChangeListener(changeListener);
		eleCorr.setWaypointGroups(active.getGroups(ActiveGpxObjects.SEG_ALL));
		eleCorr.setRunInBackground(true);
		
		mapPanel.add(progressWidget);
		mapPanel.validate();
		eleCorr.correct();
		
    }

    /**
     * Sets a flag to synchronize I/O operations with {@link GPXFile}s.  Must be called before and after each I/O.
     */
    @Deprecated
    private void setFileIOHappening(boolean happening) {
        // fileIOHappening = happening;
        updateButtonVisibility();
    }

    /**
     * retrieve GPS data from a connected device.
     */
    private void getFromDevice() {
    	        	
    	try {
			MoveBikeCompMPT move = new MoveBikeCompMPT();
			move.getConnectionParams().put("DBFILE", "D:\\Projekte\\Touring\\Temp\\bikecomp.db");
			move.connect();
			for (OnlineTrack entry : move.getTracklist()) {				
				if (entry.getId() > 200) {
					System.out.println(entry.getId());
					GPXFile gpx = move.load(entry);
					active.newGpxFile(gpx);							
				}
			}
			move.disconnect();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }

    /**
     * temp. method
     */
    private void correlate() {
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat hrmdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    	// hrmdf.setTimeZone(// Loca);
    	
		chooserFileOpen.setCurrentDirectory(new File(conf.getLastOpenDirectory()));		
        chooserFileOpen.showOpenDialog(frame);
        File file = chooserFileOpen.getSelectedFile();                
        FileReader fileReader = null;
        int i = 0;
        List<Waypoint> wpts = active.getGroups().get(0).getWaypoints();
        int max = wpts.size() - 1;
        
		try {
			String filename = file.getName().replace(".csv", "");
			Date startDate = hrmdf.parse(filename); // start date/time of HRM log
			Date firstDate = wpts.get(0).getTime(); // in UTC
			
			System.out.println(startDate.toString());
			
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = reader.readLine(); // skip CSV header
			while((line = reader.readLine()) != null) {				
				String[] fields = line.split(",");
				String timeField = fields[0];
				if (timeField.length() == 5) {
					timeField = "00:" + timeField;
				}
				// this date shit sucks so much!!!
				// int seconds = (int) (sdf.parse(timeField).getTime() / 1000); // hh:mm:ss converted to seconds
				String[] hms = timeField.split(":");
				int h = Integer.parseInt(hms[0]);
				int m = Integer.parseInt(hms[1]);
				int s = Integer.parseInt(hms[2]);
				int seconds = h * 3600 + m * 60 + s;
				
				int hr = Integer.parseInt(fields[1]); // heartrate				
				Date pointDate = new Date(startDate.getTime() + seconds * 1000);		
			
				if (pointDate.compareTo(firstDate) >= 0) {
					while (i < max) {
						System.out.println(wpts.get(i).getTime().toString() + " / " + pointDate.toString() + " / " + wpts.get(i + 1).getTime().toString());
						
						if ((pointDate.compareTo(wpts.get(i).getTime()) >= 0) && (pointDate.compareTo(wpts.get(i + 1).getTime()) <= 0)) {
							// found
							if (wpts.get(i).getExtension().containsKey(Const.EXT_HRMHR) == false) {
								wpts.get(i).getExtension().add(Const.EXT_HRMHR, Integer.toString(hr));
								break;
							}
						}
					}
				}
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void totalDist() {
    	double dist = 0.0;
		chooserFileOpen.setCurrentDirectory(new File(conf.getLastOpenDirectory()));
		chooserFileOpen.setMultiSelectionEnabled(true);		
        int returnVal = chooserFileOpen.showOpenDialog(frame);
        try {
        	GpxLoader loader = new GpxLoader();
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	for (File file : chooserFileOpen.getSelectedFiles()) {
	        			        		
					loader.open(file);
					GPXFile gpx = loader.load();
					gpx.updateAllProperties();
//					if (gpx.getLengthMeters() / 1000 > 70) {
						System.out.println(file.getName());
					//}
	        		dist += gpx.getLengthMeters();
	        		loader.close();
	        	}
	        }
	        System.out.println(dist / 1000f);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    private void doDebug() {
    	// correlate();

    }
}
