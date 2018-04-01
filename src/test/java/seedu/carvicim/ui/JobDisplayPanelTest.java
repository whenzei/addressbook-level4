package seedu.carvicim.ui;

public class JobDisplayPanelTest extends GuiUnitTest {
    /*private PersonPanelSelectionChangedEvent selectionChangedEventStub;

    private JobDisplayPanel jobDisplayPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        selectionChangedEventStub = new PersonPanelSelectionChangedEvent(new PersonCard(ALICE, 0));

        guiRobot.interact(() -> jobDisplayPanel = new JobDisplayPanel());
        uiPartRule.setUiPart(jobDisplayPanel);

        browserPanelHandle = new BrowserPanelHandle(jobDisplayPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        URL expectedDefaultPageUrl = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        assertEquals(expectedDefaultPageUrl, browserPanelHandle.getLoadedUrl());

        // associated web page of a employee
        postNow(selectionChangedEventStub);
        URL expectedPersonUrl = new URL(JobDisplayPanel.SEARCH_PAGE_URL
                + ALICE.getName().fullName.replaceAll(" ", "%20"));

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedPersonUrl, browserPanelHandle.getLoadedUrl());
    }
    */
}
