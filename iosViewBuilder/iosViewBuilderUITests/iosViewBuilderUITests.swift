// Created 4/10/23
// swift-tools-version:5.0

import XCTest

final class iosViewBuilderUITests: XCTestCase {

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.

        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testSurveyA() throws {
        // UI tests must launch the application that they test.
        let app = XCUIApplication()
        app.launch()

        // Use XCTAssert and related functions to verify your tests produce the correct results.
        
        app.collectionViews/*@START_MENU_TOKEN@*/.buttons["Example Survey A"]/*[[".cells.buttons[\"Example Survey A\"]",".buttons[\"Example Survey A\"]"],[[[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.tap()
        app.buttons["Start"].tap()
        
        let scrollViewsQuery = app.scrollViews
        let elementsQuery = scrollViewsQuery.otherElements
        let nextButton = elementsQuery.buttons["Next"]
        
        // Select Birth year
        elementsQuery.switches["Birth year"].tap()
        nextButton.tap()
        
        // Enter a year of birth
        elementsQuery.textFields["YYYY"].tap()
        elementsQuery.textFields["YYYY"].typeText("1958")
        app.toolbars["Toolbar"].buttons["Done"].tap()
        nextButton.tap()
        
        // Select Yes
        elementsQuery.switches["Yes"].tap()
        nextButton.tap()
        
        // Select Pizza
        elementsQuery.switches["Sushi"].tap()
        nextButton.tap()
        
        // Select Blue + Red
        elementsQuery.switches["Blue"].tap()
        elementsQuery.switches["Red"].tap()
        elementsQuery.buttons["Submit"].tap()
        
        // Exit
        app.buttons["Exit"].tap()
    }
    
    func testSurveyB() throws {
        
        let app = XCUIApplication()
        app.launch()
        
        app.collectionViews/*@START_MENU_TOKEN@*/.buttons["Example Survey B"]/*[[".cells.buttons[\"Example Survey B\"]",".buttons[\"Example Survey B\"]"],[[[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.tap()
        
        app.buttons["Start"].tap()
        
        let elementsQuery = app.scrollViews.otherElements
        let nextButton = elementsQuery.buttons["Next"]
        
        elementsQuery.switches["Blue"].tap()
        nextButton.tap()
        elementsQuery.switches["Green"].tap()
        nextButton.tap()
        elementsQuery.switches["Pizza"].tap()
        nextButton.tap()
        elementsQuery.switches["Ice Cream"].tap()
        nextButton.tap()
        
        app.buttons["Exit"].tap()
    }

}
