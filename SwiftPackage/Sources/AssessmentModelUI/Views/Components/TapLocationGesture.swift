//
//  TapLocationGesture.swift
//
//

import SwiftUI

// Modified from https://stackoverflow.com/questions/56513942/how-to-detect-a-tap-gesture-location-in-swiftui

struct TapLocationGesture: Gesture {
    let count: Int
    let coordinateSpace: CoordinateSpace
    
    typealias Value = SimultaneousGesture<TapGesture, DragGesture>.Value
    
    init(count: Int = 1, coordinateSpace: CoordinateSpace = .local) {
        precondition(count > 0, "Count must be greater than or equal to 1.")
        self.count = count
        self.coordinateSpace = coordinateSpace
    }
    
    var body: SimultaneousGesture<TapGesture, DragGesture> {
        SimultaneousGesture(
            TapGesture(count: count),
            DragGesture(minimumDistance: 0, coordinateSpace: coordinateSpace)
        )
    }
    
    func onEnded(perform action: @escaping (CGPoint) -> Void) -> _EndedGesture<TapLocationGesture> {
        self.onEnded { (value: Value) -> Void in
            guard value.first != nil else { return }
            guard let location = value.second?.startLocation else { return }
            guard let endLocation = value.second?.location else { return }
            guard ((location.x-1)...(location.x+1)).contains(endLocation.x),
                  ((location.y-1)...(location.y+1)).contains(endLocation.y) else {
                return
            }
            action(location)
        }
    }
}

extension View {
    func onTapLocationGesture(
        count: Int = 1,
        coordinateSpace: CoordinateSpace = .local,
        perform action: @escaping (CGPoint) -> Void
    ) -> some View {
        gesture(TapLocationGesture(count: count, coordinateSpace: coordinateSpace)
            .onEnded(perform: action)
        )
    }
}
