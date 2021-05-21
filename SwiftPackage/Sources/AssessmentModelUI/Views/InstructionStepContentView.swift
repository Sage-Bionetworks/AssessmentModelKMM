//
//  InstructionStepView.swift
//
//  Copyright © 2021 Sage Bionetworks. All rights reserved.
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

struct InstructionStepContentView : View {
    let node: ContentNodeStep
    
    var body: some View {
        ScrollView {
            // Only show the image if the font size is not extra large.
            let fontRatio: CGFloat = Font.fontRatio()
            if let imageName = node.imageInfo?.imageName,
                fontRatio < 1.5 {
                
                // If the accessibility for text is large then shrink the image
                // so that it doesn't take the whole screen.
                let imageRatio: CGFloat = (fontRatio > 1.0) ? (1.0 / fontRatio) : 1.0
                let height = 160.0 * imageRatio
                
                Image(imageName)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(height: height, alignment: .center)
                    .padding(.top, 10.0)
                    .padding(.bottom, 44.0)
            }
            Text(node.title ?? "")
                .font(.instructionTitle)
                .padding(.bottom, 14.0)
                .padding(.horizontal, 60.0)
            Text(node.detail ?? "")
                .font(.instructionDetail)
                .padding(.horizontal, 56.0)
                .padding(.bottom, 14.0)
                .lineLimit(nil)
                .fixedSize(horizontal: false, vertical: true)
                .frame(maxHeight: .infinity)
        }
    }
}

#if PREVIEW

struct InstructionStepContentView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            InstructionStepContentView(node: previewPermissionsNodes[0])
            InstructionStepContentView(node: previewPermissionsNodes[1])
            InstructionStepContentView(node: previewPermissionsNodes[2])
        }
    }
}

#endif
