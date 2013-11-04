#!/bin/bash

size=16x16
w=16

texW=4
texH=4

working=/tmp/hexel/genAtlas
tex=img/tex.png
dmg=img/damage.png
atlas=img/atlas.png

rm -r /tmp/hexel/genAtlas/*
mkdir -p $working

convert -size $size xc:none $working/damage0.png
convert -crop $size+$[$w*0]x0 $dmg $working/damage1.png
convert -crop $size+$[$w*1]x0 $dmg $working/damage2.png
convert -crop $size+$[$w*2]x0 $dmg $working/damage3.png
convert -crop $size+$[$w*3]x0 $dmg $working/damage4.png

dmgTex(){
  mapN=$1
files=`python << END
print('$working/damage$mapN.png '*$texW*$texH)
END`
  montage $files \
             -background none -mode Concatenate -tile $texWx$texH $working/damageMap$mapN.png

  convert $tex null: $working/damageMap$mapN.png -layers Composite $working/texDamage$mapN.png
}


dmgTex 0
dmgTex 1
dmgTex 2
dmgTex 3
dmgTex 4

  montage $working/texDamage0.png $working/texDamage1.png $working/texDamage2.png $working/texDamage3.png $working/texDamage4.png \
             -background none -mode Concatenate -tile x1 $atlas
