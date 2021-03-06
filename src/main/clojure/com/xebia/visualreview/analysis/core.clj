;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Copyright 2015 Xebia B.V.
;
; Licensed under the Apache License, Version 2.0 (the "License")
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;     http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns com.xebia.visualreview.analysis.core
  (:require [com.xebia.visualreview.service-util :as sutil])
  (:import [com.xebia.visualreview PixelComparator DiffReport]
           [javax.imageio ImageIO]
           (java.io File)))

(defn generate-diff-report
  "Takes 2 inputfiles and returns a map with:
  :diff => A image file of the diff,
  :percentage => A double with the percentage difference found"
  [file1 file2]
  (let [result ^DiffReport (PixelComparator/processImage file1 file2)
        diff-file (File/createTempFile "vr-diff-" ".tmp")
        write-success? (ImageIO/write (.getDiffImage result) "png" diff-file)]
    (do
      (sutil/assume (true? write-success?) (str "Could not write diff image to temporary file " (.getAbsolutePath() diff-file)) ::diff-could-not-write-on-fs))
      {:diff diff-file
       :percentage (.getPercentage result)}))
