//
//  AssessmentFactory.swift
//  
//
//  Copyright © 2017-2022 Sage Bionetworks. All rights reserved.
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

import Foundation
import JsonModel

open class AssessmentFactory : SerializationFactory {
    
    public static var shared = AssessmentFactory.defaultFactory

//    public let inputItemSerializer = InputItemSerializer()
    
//    public let taskSerializer = TaskSerializer()
    
    public let answerTypeSerializer = AnswerTypeSerializer()
    public let buttonActionSerializer = ButtonActionSerializer()
    public let imageInfoSerializer = ImageInfoSerializer()
    public let questionSerializer = QuestionSerializer()
    public let textInputItemSerializer = TextInputItemSerializer()
    public let resultSerializer = ResultDataSerializer()
    public let stepSerializer = NodeSerializer()
    
    public required init() {
        super.init()
        self.registerSerializer(answerTypeSerializer)
        self.registerSerializer(resultSerializer)
        self.registerSerializer(imageInfoSerializer)
        self.registerSerializer(buttonActionSerializer)
        self.registerSerializer(textInputItemSerializer)
        self.registerSerializer(stepSerializer)
        self.registerSerializer(questionSerializer)
    }
    
}
