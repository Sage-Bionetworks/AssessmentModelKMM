//
//  ViewHeightReader.swift
//
//

import SwiftUI

public extension View {
    func heightReader(height: Binding<CGFloat>) -> some View {
        modifier(ViewDimensionReader(height))
    }
    func widthReader(width: Binding<CGFloat>) -> some View {
        modifier(ViewDimensionReader(width, isHeight: false))
    }
}

struct ViewDimensionReader : ViewModifier {
    @Binding var dim: CGFloat
    let isHeight: Bool
    
    init(_ dim: Binding<CGFloat>, isHeight: Bool = true) {
        self._dim = dim
        self.isHeight = isHeight
    }
    
    func body(content: Content) -> some View {
        content
            .background(GeometryReader {
                Color.clear.preference(key: ViewDimensionKey.self,
                                       value: self.isHeight ? $0.frame(in: .local).size.height : $0.frame(in: .local).size.width
                )
            })
            .onPreferenceChange(ViewDimensionKey.self) {
                if dim != $0 { dim = $0 }   // Only set if it changed
            }
    }
}

struct ViewDimensionKey: PreferenceKey {
    static var defaultValue: CGFloat { 0 }
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value = value + nextValue()
    }
}
