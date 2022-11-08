//
//  CompositedImage.swift
//
//

import SwiftUI

public struct CompositedImage: View {
    @SwiftUI.Environment(\.surveyTintColor) var surveyTint: Color
    let imageName: String
    let layerCount: Int
    let bundle: Bundle?
    let isResizable: Bool
    
    public init(_ imageName: String, bundle: Bundle? = nil, layers: Int = 2, isResizable: Bool = false) {
        self.imageName = imageName
        self.layerCount = layers
        self.bundle = bundle
        self.isResizable = isResizable
    }
    
    public var body: some View {
        ZStack {
            ForEach(0 ..< layerCount, id:\.self) { layer in
                Image(decorative: "\(imageName).\(layer)", bundle: bundle)
                    .fillFit(isResizable)
            }
        }
        .foregroundColor(surveyTint)
    }
}

extension Image {
    
    @ViewBuilder
    func fillFit(_ isResized: Bool) -> some View {
        if isResized {
            self.resizable().scaledToFit()
        }
        else {
            self
        }
    }
}

struct LayeredImageView_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack {
                CompositedImage("survey", bundle: .module, layers: 4)
                CompositedImage("survey", bundle: .module, layers: 4)
                    .surveyTintColor(.red)
                CompositedImage("survey", bundle: .module, layers: 4, isResizable: true)
            }
        }
    }
}
