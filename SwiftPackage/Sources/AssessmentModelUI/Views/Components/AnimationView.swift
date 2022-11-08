//
//  AnimationView.swift
//
//

import SwiftUI
import AssessmentModel

public struct AnimationView: View {
    
    let bundle: Bundle?
    let duration: TimeInterval
    let loops: Int
    let animatedImageNames: [String]
    @State var loopCount: Int
    @State var animatedImageIndex: Int
    @State var timer: Timer?
    
    public init(animatedImageInfo: AnimatedImageInfo) {
        self.animatedImageNames = animatedImageInfo.imageNames
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
    
    fileprivate func handleLoop() {
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
    
    func start() {
        timer = Timer.scheduledTimer(withTimeInterval: duration / Double(animatedImageNames.count), repeats: true) {_ in
            handleLoop()
        }
    }
    
    func stop() {
        timer?.invalidate()
    }
}

struct AnimationView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AnimationView(animatedImageInfo: infiniteLoopExample)
            AnimationView(animatedImageInfo: finiteLoopExample)
        }
    }
}

fileprivate let infiniteLoopExample = AnimatedImage(imageNames: imageNamesExample, animationDuration: 1)

fileprivate let finiteLoopExample = AnimatedImage(imageNames: imageNamesExample, animationDuration: 1, animationRepeatCount: 3)

fileprivate let imageNamesExample = ["TapLeft1", "TapLeft2"]
