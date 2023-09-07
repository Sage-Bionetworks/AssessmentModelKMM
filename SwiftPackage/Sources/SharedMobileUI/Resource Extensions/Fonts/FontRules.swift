//
//  FontRules.swift
//

import SwiftUI

/// A protocol that can be used to return a set of consistent font sizes, style, weights, and colors for the
/// ``Text`` displayed in a given app.
///
/// The font rules for an app can be accessed as a static property on the  ``DesignSystem``.
///
/// ```swift
///     Text("Hello, World")
///         .font(DesignSystem.fontRules.bodyFont(at: 2, isEmphasis: false))
/// ```
public protocol FontRules {
    
    /// The maximum supported header level returned by the method ``headerFont(at:)``.
    var maxSupportedHeaderLevel: Int { get }
    
    /// Return the ``Font`` for this ``level`` of header text.
    ///
    /// - Parameter level: The incremental level (h1, h2, h3, etc.) of the header.
    /// - Returns: The ``Font`` for this level of text.
    func headerFont(at level: Int) -> Font
    
    /// The maximum supported body level returned by the method ``bodyFont(at:isEmphasis:)``.
    var maxSupportedBodyLevel: Int { get }
    
    /// Returns the ``Font`` for this level of paragraph text.
    ///
    /// - Parameters:
    ///   - level: The incremental level (p1, p2, etc.) of the paragraph.
    ///   - isEmphasis: Whether or not this text should be emphasized.
    /// - Returns: The ``Font`` for this level of text.
    func bodyFont(at level: Int, isEmphasis: Bool) -> Font
    
    /// The maximum supported button level returned by the method ``buttonFont(at:isSelected:)``.
    var maxSupportedButtonLevel: Int { get }
    
    /// Returns the ``Font`` for this button level and selection state.
    ///
    /// - Parameters:
    ///   - level: The incremental level (btn1, btn2, etc.) of the button.
    ///   - isSelected: Whether or not the button is selected.
    /// - Returns: The ``Font`` for this level of text.
    func buttonFont(at level: Int, isSelected: Bool) -> Font
    
    /// Returns the ``Color`` for this button level and selection state.
    ///
    /// - Parameters:
    ///   - level: The incremental level (btn1, btn2, etc.) of the button.
    ///   - isSelected: Whether or not the button is selected.
    /// - Returns: The ``Color`` for this button level.
    func buttonColor(at level: Int, isSelected: Bool) -> Color
}

/// A public static accessor for accessing shared fonts used throughout an app.
public final class DesignSystem {
    
    /// The ``FontRules`` set by this app *or* the default font rules if not explicitly set.
    public static var fontRules : FontRules = DefaultFontRules()
}

enum HeaderLevel: Int, CaseIterable {
    case h1 = 1, h2, h3, h4, h5, h6, h7
}

enum BodyLevel: Int, CaseIterable {
    case body1 = 1, body2
}

enum ButtonLevel: Int, CaseIterable {
    case button1 = 1, button2, underlinedButton
}

extension FontRules {
    func headerFont(at level: HeaderLevel) -> Font {
        headerFont(at: level.rawValue)
    }
    func bodyFont(at level: BodyLevel, isEmphasis: Bool = false) -> Font {
        bodyFont(at: level.rawValue, isEmphasis: isEmphasis)
    }
    func buttonFont(at level: ButtonLevel, isSelected: Bool = false) -> Font {
        buttonFont(at: level.rawValue, isSelected: isSelected)
    }
}

struct DefaultFontRules : FontRules {
    
    var maxSupportedHeaderLevel: Int { HeaderLevel.allCases.count }
    
    func headerFont(at level: Int) -> Font {
        switch HeaderLevel(rawValue: level) {
        case .h1:
            return .latoFont(21, relativeTo: .largeTitle)
        case .h2:
            return .latoFont(18, relativeTo: .largeTitle)
        case .h3:
            return .latoFont(24, relativeTo: .title, weight: .bold)
        case .h4:
            return .latoFont(18, relativeTo: .title, weight: .bold)
        case .h5:
            return .latoFont(16, relativeTo: .title2, weight: .bold)
        case .h6:
            return .poppinsFont(14, relativeTo: .title3, weight: .medium)
        case .h7:
            return .poppinsFont(12, relativeTo: .title3, weight: .medium)
        case .none:
            debugPrint("WARNING: Level \(level) headers are not supported by this FontRules.")
            return .poppinsFont(12, relativeTo: .title3, weight: .medium)
        }
    }
    
    var maxSupportedBodyLevel: Int { BodyLevel.allCases.count }
    
    func bodyFont(at level: Int, isEmphasis: Bool) -> Font {
        let weight: Font.Weight = Font.isBoldTextEnabled || isEmphasis ? .bold : .medium
        let fontSize: CGFloat = {
            switch BodyLevel(rawValue: level) {
            case .body1:
                return 16
            case .body2:
                return 14
            case .none:
                debugPrint("WARNING: Level \(level) body are not supported by this FontRules.")
                return 14
            }
        }()
        if Font.isBoldTextEnabled && isEmphasis {
            return .italicLatoFont(fontSize, relativeTo: .body, weight: weight)
        }
        else {
            return .latoFont(fontSize, relativeTo: .body, weight: weight)
        }
    }
    
    var maxSupportedButtonLevel: Int { ButtonLevel.allCases.count }
    
    func buttonFont(at level: Int, isSelected: Bool) -> Font {
        switch ButtonLevel(rawValue: level) {
        case .button1:
            return .latoFont(fixedSize: 20, weight: .bold)
        case .button2:
            return .latoFont(fixedSize: 14, weight: isSelected ? .bold : .medium)
        case .underlinedButton:
            return .latoFont(fixedSize: 18, weight: .regular)
        case .none:
            debugPrint("WARNING: Level \(level) buttons are not supported by this FontRules.")
            return .latoFont(fixedSize: 12, weight: isSelected ? .bold : .medium)
        }
    }
    
    func buttonColor(at level: Int, isSelected: Bool) -> Color {
        if isSelected && ButtonLevel(rawValue: level) == .button2 && Font.isBoldTextEnabled {
            return .textLinkColor
        }
        else {
            return .textForeground
        }
    }
}
