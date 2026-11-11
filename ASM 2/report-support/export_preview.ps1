$docPath = 'D:\COS30017-Software Development for Mobile Devices\GitHub-Repo\ASM 2\COS30017 A2 - Rent a Car with Intent (DriveEase) Report - FINAL.docx'
$pdfPath = 'D:\COS30017-Software Development for Mobile Devices\GitHub-Repo\ASM 2\report-support\report_preview.pdf'
$word = New-Object -ComObject Word.Application
$word.Visible = $false
$doc = $word.Documents.Open($docPath, $false, $true)
$doc.ExportAsFixedFormat($pdfPath, 17)
$doc.Close(0)
$word.Quit()
Write-Output 'EXPORTED'
