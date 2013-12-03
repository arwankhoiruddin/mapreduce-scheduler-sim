
PDF = cloudsim.pdf

DATA = SS.dat TS.dat TT.dat ST.dat

IMG = resources.png init5_4000.png fin5_4000.png

PDFLATEX = pdflatex

GNUPLOT = gnuplot


all: $(IMG) $(PDF)

%.png :  %.p $(DATA)
	$(GNUPLOT) $*.p

%.pdf : %.tex $(IMG) $(DATA)
	$(PDFLATEX) $*.tex


clean: 
	rm $(PDF) $(IMG)
