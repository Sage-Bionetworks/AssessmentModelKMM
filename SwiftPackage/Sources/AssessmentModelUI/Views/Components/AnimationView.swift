//
//  AnimationView.swift
//
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

import SwiftUI
import AssessmentModel

public struct AnimationView: View {
    
    let bundle: Bundle?
    let duration: TimeInterval
    let loops: Int
    fileprivate var animatedImageNames: [String] = []
    @State var loopCount: Int
    @State var animatedImageIndex: Int
    @State var timer: Timer?
    
    public init(animatedImageInfo: AnimatedImageInfo) {
        for imageName in animatedImageInfo.imageNames {
            self.animatedImageNames.append(imageName)
        }
        self.bundle = animatedImageInfo.bundle
        self.duration = animatedImageInfo.animationDuration
        self.loops = animatedImageInfo.animationRepeatCount ?? 0
        self.loopCount = loops
        self.animatedImageIndex = 0
    }
    
    public var body: some View {
        Image(animatedImageNames[animatedImageIndex], bundle: bundle)
            .onAppear{
                start()
            }
            .onDisappear{
                stop()
            }
    }
    
    func start() {
        timer = Timer.scheduledTimer(withTimeInterval: duration / Double(animatedImageNames.count), repeats: true) {_ in
            let index = (animatedImageIndex + 1) % animatedImageNames.count
            if index == 0 {
                loopCount -= 1
            }
            if loopCount == 0, loops > 0 {
                stop()
            }
            else {
                animatedImageIndex = index
            }
        }
    }
    
    func stop() {
        timer?.invalidate()
    }
}

struct AnimationView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AnimationView(animatedImageInfo: animationExample1)
            AnimationView(animatedImageInfo: animationExample2)
                .preferredColorScheme(.dark)
        }
    }
}

fileprivate let animationExample1 = AnimatedImage(imageNames: imageNamesExample, animationDuration: 3)

fileprivate let animationExample2 = AnimatedImage(imageNames: imageNamesExample, animationDuration: 1, animationRepeatCount: 3)

fileprivate let imageNamesExample = ["TapLeft1", "TapLeft2"]
