
rem ref: http://stackoverflow.com/questions/7659466/batch-script-for-pdflatex-makeindex-bibtex-that-works-on-current-file-in-notepad

:: Called from Notepad++ Run
:: [path_to_bat_file] "$(CURRENT_DIRECTORY)" "$(NAME_PART)"

:: Change Drive and  to File Directory
rem %~d1
rem cd %1

:: Run Cleanup
rem call:cleanup
rem tskill acrobat  
rem pdflatex %2.tex
rem bibtex %2
rem pdflatex %2.tex
rem pdflatex %2.tex
rem makeindex.exe %2.nlo -s nomencl.ist -o %2.nls
rem pdflatex %2.tex
rem START "" %2.pdf

:cleanup
:: del *.log
del /s *.log
del /s *.idx
del /s  *.toc
del /s  *.dvi
del /s  *.aux
del /s  *.bbl
del /s  *.blg
del /s  *.brf
del /s  *.out
del /s  *.gz
del /s  *.ilg
del /s  *.nlo
del /s  *.lof
del /s  *.lot
rem del /s  *.pdf
goto:eof


