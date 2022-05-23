//
//  LocalTimeTests.swift
//
//  Copyright © 2022 Sage Bionetworks. All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1.  Redistributions of source code must retain the above copyright notice, this
// list of conditions and the following disclaimer.
//
// 2.  Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation and/or
// other materials provided with the distribution.
//
// 3.  Neither the name of the copyright holder(s) nor the names of any contributors
// may be used to endorse or promote products derived from this software without
// specific prior written permission. No license is granted to the trademarks of
// the copyright holders even if such marks are included in this software.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//

import XCTest
@testable import AssessmentModel
import JsonModel

class LocalTimeTests: XCTestCase {
    
    let decoder = AssessmentFactory().createJSONDecoder()
    let encoder = AssessmentFactory().createJSONEncoder()

    override func setUp() {
        // Use a statically defined timezone.
        ISO8601TimeOnlyFormatter.timeZone = TimeZone(secondsFromGMT: Int(-2.5 * 60 * 60))
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }
    
    func testLocalTimeBounds() {
        let time1 = LocalTime(hour: 24, minute: 1)
        XCTAssertNil(time1)
        
        let time2 = LocalTime(hour: 24, minute: 0)
        XCTAssertNotNil(time2)
        XCTAssertEqual(.max, time2)
        
        let time3 = LocalTime(hour: 0, minute: 0)
        XCTAssertNotNil(time3)
        XCTAssertEqual(.min, time3)
        
        let time4 = LocalTime(hour: -1, minute: 0)
        XCTAssertNil(time4)
        
        let time5 = LocalTime(hour: 0, minute: -1)
        XCTAssertNil(time5)
        
        let time6 = LocalTime(hour: 0, minute: 60)
        XCTAssertNil(time6)
    }
    
    func testLocalTimeToString() {
        let time1 = LocalTime(hour: 14, minute: 30)
        XCTAssertEqual("14:30:00.000", time1?.stringValue())
        
        let time2 = LocalTime(hour: 24, minute: 0)
        XCTAssertEqual("24:00:00.000", time2?.stringValue())
        
        let time3 = LocalTime(hour: 0, minute: 0)
        XCTAssertEqual("00:00:00.000", time3?.stringValue())
    }
    
    func testLocalTimeToDate() {
        let calendar = Calendar(identifier: .iso8601)
        let now = Date()
        let startOfDay = calendar.startOfDay(for: now)
        let midDay = calendar.date(bySettingHour: 12, minute: 0, second: 0, of: startOfDay)!
        XCTAssertTrue(calendar.isDate(midDay, inSameDayAs: now))
        
        guard let time1 = LocalTime(hour: 6, minute: 30),
              let time2 = LocalTime(hour: 14, minute: 45)
        else {
            XCTFail("Failed to create valid LocalTime objects")
            return
        }

        let date1 = time1.date(from: midDay)
        let date2 = time2.date(from: midDay)
        
        XCTAssertLessThan(date1, date2)
        XCTAssertLessThan(date1, midDay)
        XCTAssertLessThan(midDay, date2)

        XCTAssertTrue(calendar.isDate(date1, inSameDayAs: now))
        XCTAssertTrue(calendar.isDate(date2, inSameDayAs: now))
        
        let dateComponents1 = calendar.dateComponents([.hour, .minute], from: date1)
        XCTAssertEqual(6, dateComponents1.hour)
        XCTAssertEqual(30, dateComponents1.minute)
        
        let dateComponents2 = calendar.dateComponents([.hour, .minute], from: date2)
        XCTAssertEqual(14, dateComponents2.hour)
        XCTAssertEqual(45, dateComponents2.minute)
    }
    
    func testLocalTimeCoding() {
        let json = """
            {
                "minimumValue": "06:30:02.123",
                "maximumValue": "24:00:00.000",
            }
        """.data(using: .utf8)! // our data in native (JSON) format
        
        do {
            let expectedObject = TimeFormatOptions(from: .init(hour: 6, minute: 30)!, to: .max)
            let decodedObject = try decoder.decode(TimeFormatOptions.self, from: json)
            XCTAssertEqual(expectedObject, decodedObject)
            
            let jsonData = try encoder.encode(expectedObject)
            guard let dictionary = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String : String]
            else {
                XCTFail("Encoded object is not a dictionary")
                return
            }
            
            XCTAssertEqual("06:30:00.000", dictionary["minimumValue"])
            XCTAssertEqual("24:00:00.000", dictionary["maximumValue"])
            
        } catch {
            XCTFail("Failed to decode/encode object: \(error)")
            return
        }
    }
}
