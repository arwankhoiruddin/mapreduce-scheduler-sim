
PDF = cloudsim.pdf

DATA = SS.dat TS.dat TT.dat ST.dat SO.dat TO.dat DT.dat DS.dat

IMG = resources.png init5_4000.png fin5_4000.png exec5_4000.png classDiagram.png finish.png

PDFLATEX = pdflatex

GNUPLOT = gnuplot


all: $(IMG) $(PDF)

%.png :  %.p $(DATA)
	$(GNUPLOT) $*.p

%.pdf : %.tex $(IMG) $(DATA)
	$(PDFLATEX) $*.tex


clean: 
	rm $(PDF) $(IMG)
