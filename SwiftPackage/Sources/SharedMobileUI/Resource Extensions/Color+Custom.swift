//
//  Color+Custom.swift
//

import SwiftUI

#if os(macOS)
import AppKit
fileprivate typealias TypedColor = NSColor
#elseif canImport(UIKit)
import UIKit
fileprivate typealias TypedColor = UIColor

extension UIColor {
    public static let textForeground: UIColor = .init(named: "textForeground", in: .module, compatibleWith: nil) ?? .label
}
#endif

extension Color {
    
    // Named colors
    public static let sageBlack: Color = .init("sageBlack", bundle: .module)
    public static let sageWhite: Color = .init("sageWhite", bundle: .module)
    public static let screenBackground: Color = .init("screenBackground", bundle: .module)
    public static let textForeground: Color = .init("textForeground", bundle: .module)
    
    // App colors
    public static let textLinkColor: Color = {
        if let _ = TypedColor.init(named: "LinkColor") {
            return .init("LinkColor")
        }
        else {
            return .accentColor
        }
    }()
    
    // Shades of gray
    public static let hex2A2A2A: Color = .init("#2A2A2A", bundle: .module)
    public static let hex3E3E3E: Color = .init("#3E3E3E", bundle: .module)
    public static let hex727272: Color = .init("#727272", bundle: .module)
    public static let hexB8B8B8: Color = .init("#B8B8B8", bundle: .module)
    public static let hexDEDEDE: Color = .init("#DEDEDE", bundle: .module)
    public static let hexDFDFDF: Color = .init("#DFDFDF", bundle: .module)
    public static let hexE5E5E5: Color = .init("#E5E5E5", bundle: .module)
    public static let hexF0F0F0: Color = .init("#F0F0F0", bundle: .module)
    public static let hexF6F6F6: Color = .init("#F6F6F6", bundle: .module)
    public static let hexFDFDFD: Color = .init("#FDFDFD", bundle: .module)
    
    /// Initialize with the hex color.
    public init?(hex: String) {
        let hexString = hex.hasPrefix("#") || hex.hasPrefix("0x") ? String(hex.suffix(6)) : hex
        if hexString == "FFFFFF" || hexString == "white" {
            self.init(white: 1)
        }
        else if hexString == "000000" || hexString == "black" {
            self.init(white: 0)
        }
        else if let (r,g,b) = rbgValues(from: hexString) {
            self.init(red: r, green: g, blue: b)
        }
        else {
            return nil
        }
    }
}

fileprivate func rbgValues(from hexColor: String) -> (red: Double, green: Double, blue: Double)? {
    
    // If there aren't 6 characters in the hex color then return nil.
    guard hexColor.count == 6 else {
        debugPrint("WARNING! hexColor '\(hexColor)' does not have 6 characters.")
        return nil
    }
    
    let scanner = Scanner(string: hexColor)
    var hexNumber: UInt64 = 0
        
    // Scan the string into a hex.
    guard scanner.scanHexInt64(&hexNumber) else {
        debugPrint("WARNING! hexColor '\(hexColor)' failed to scan.")
        return nil
    }
    
    let r = Double((hexNumber & 0xff0000) >> 16) / 255
    let g = Double((hexNumber & 0x00ff00) >> 8) / 255
    let b = Double((hexNumber & 0x0000ff) >> 0) / 255
    
    return (r, g, b)
}
