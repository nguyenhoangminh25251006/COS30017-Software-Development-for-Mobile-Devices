Add-Type -AssemblyName System.Drawing

$dir = 'D:\COS30017-Software Development for Mobile Devices\GitHub-Repo\ASM 2\report-support'
$outDir = Join-Path $dir 'figures\final'

function Load-Image($p) { [System.Drawing.Image]::FromFile($p) }

# ---------- Figure 9: landscape catalogue (two panels) ----------
$top = Load-Image (Join-Path $dir 'landscape_dashboard_final.png')
$bottom = Load-Image (Join-Path $dir 'landscape_dashboard_final2.png')

$canvasW = 1200
$panelW = 1160
$panelH = [int](1080 * ($panelW / 2424.0))
$labelH = 40
$gap = 18
$canvasH = 20 + $labelH + $panelH + $gap + $labelH + $panelH + 20

$bmp = New-Object System.Drawing.Bitmap $canvasW, $canvasH
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.Clear([System.Drawing.Color]::White)
$g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
$g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
$g.TextRenderingHint = [System.Drawing.Text.TextRenderingHint]::ClearTypeGridFit

$font = New-Object System.Drawing.Font 'Segoe UI', 19, ([System.Drawing.FontStyle]::Bold)
$brush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(17, 24, 39))

$x = [int](($canvasW - $panelW) / 2)
$y1 = 20
$g.DrawString('(a) Side-by-side header, search and sort with the Popular Cars row', $font, $brush, $x, $y1)
$g.DrawImage($top, $x, ($y1 + $labelH), $panelW, $panelH)

$y2 = $y1 + $labelH + $panelH + $gap
$g.DrawString('(b) Scrolled dashboard: Favourites and Active Rentals side by side', $font, $brush, $x, $y2)
$g.DrawImage($bottom, $x, ($y2 + $labelH), $panelW, $panelH)

$g.Dispose()
$bmp.Save((Join-Path $outDir 'figure_09_landscape_catalogue.png'), [System.Drawing.Imaging.ImageFormat]::Png)
$bmp.Dispose()
$top.Dispose()
$bottom.Dispose()

# ---------- Figure 10: landscape rental ----------
$rental = Load-Image (Join-Path $dir 'landscape_rental_final.png')
$rw = 1200
$rh = [int](1080 * ($rw / 2424.0))
$bmp2 = New-Object System.Drawing.Bitmap $rw, $rh
$g2 = [System.Drawing.Graphics]::FromImage($bmp2)
$g2.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
$g2.DrawImage($rental, 0, 0, $rw, $rh)
$g2.Dispose()
$bmp2.Save((Join-Path $outDir 'figure_10_landscape_rental.png'), [System.Drawing.Imaging.ImageFormat]::Png)
$bmp2.Dispose()
$rental.Dispose()

Write-Output "DONE $canvasW x $canvasH"
