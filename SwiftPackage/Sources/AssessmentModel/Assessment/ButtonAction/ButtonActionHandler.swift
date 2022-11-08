//
//  RSDUIActionHandler.swift
//  Research
//

import Foundation

/// `ButtonActionHandler` implements the custom actions of the step.
public protocol ButtonActionHandler {
    func button(_ buttonType: ButtonType, node: Node) -> ButtonActionInfo?
    func shouldHideButton(_ buttonType: ButtonType, node: Node) -> Bool?
}
