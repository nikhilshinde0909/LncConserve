#!/bin/bash
# get paths
echo 'getting paths'
Activate_path=`which activate 2>/dev/null`
bpipe_path=`which bpipe 2>/dev/null`
hisat2_path=`which hisat2 2>/dev/null`
stringtie_path=`which stringtie 2>/dev/null`
gffread_path=`which gffread 2>/dev/null`
gffcompare_path=`which gffcompare 2>/dev/null`
samtools_path=`which samtools 2>/dev/null`
bowtie2_path=`which bowtie2 2>/dev/null`
bamToFastq_path=`which bamToFastq 2>/dev/null`
fastp_path=`which fastp 2>/dev/null`
seqtk_path=`which seqtk 2>/dev/null`
python3_path=`which python3 2>/dev/null`

# slncky
source $Activate_path slncky
python2_path=`which python 2>/dev/null`
# slncky_path=`which slncky 2>/dev/null`

# Add paths to tools.groovy
echo 'adding paths to tools.groovy'
echo "// Path to tools used by the pipeline" > ./tools.groovy
echo "Activate=\"$Activate_path\"" >> ./tools.groovy
echo "bpipe=\"$bpipe_path\"" >> ./tools.groovy
echo "hisat2=\"$hisat2_path\"" >> ./tools.groovy
echo "stringtie=\"$stringtie_path\"" >> ./tools.groovy
echo "gffread=\"$gffread_path\"" >> ./tools.groovy
echo "gffcompare=\"$gffcompare_path\"" >> ./tools.groovy
echo "samtools=\"$samtools_path\"" >> ./tools.groovy
echo "bowtie2=\"$bowtie2_path\"" >> ./tools.groovy
echo "bamToFastq=\"$bamToFastq_path\"" >> ./tools.groovy
echo "fastp=\"$fastp_path\"" >> ./tools.groovy
echo "seqtk=\"$seqtk_path\"" >> ./tools.groovy
echo "python3=\"$python3_path\"" >> ./tools.groovy
echo "" >> ./tools.groovy

echo "// Path to python 2.7 and slncky" >> ./tools.groovy
echo "python2=\"$python2_path\"" >> ./tools.groovy
# echo "slncky=\"$slncky_path\"" >> ./tools.groovy
