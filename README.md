# LncConserve
A pipeline for lncRNA identification and inter-species lncRNA conservation analysis

# Introduction
Long non-coding RNA is the major class of non-coding RNA with lower expression than mRNAs, complex expression patterns, and low conservation. The present investigation aimed to develop an automated pipeline for lncRNA identification using RNA-seq and inter-species conservation analysis. The pipeline takes raw \*.fastq.gz, rRNA sequences (\*.fa) reference genome (\*.fa), and annotations (\*.gtf) for the species and related species. Also, we have provided additional scripts to download rRNA sequences, reference genomes, and annotations from Ensembl plants.

<p align="center">
  <img src="https://github.com/nikhilshinde0909/LncConserve/blob/main/LncConserve.png" width=50% height=25%>
</p>

# Implementation
1. To execute the steps in the pipeline, download the latest release of LncConserve to your local system with the following command 
```
git clone https://github.com/nikhilshinde0909/LncConserve.git
```

2. Download and install the latest release of Mambaforge from Git Hub [https://github.com/conda-forge/miniforge] to install the required software and tools.


3. Once the Mambaforge is installed, Install the required software by updating the base environment from LncConserve.yml file as follows
```
mamba env update --file LncConserve.yml
```

  
4. Create environment for Slncky from the environment file 
```
mamba env create -f slncky.yml
```

5. Run bash script named "add_paths_for_tools.sh" to add the path of conda environments and software in tools.groovy file
```
chmod +x add_paths_for_tools.sh && bash add_paths_for_tools.sh
```
6. Prepare your inputs and data.txt in the working directory
```
mkdir data
Working directory
├── data
│   ├── SRR975551_1.fastq.gz
│   ├── SRR975552_1.fastq.gz
│   └── (and other fastq.gz files)
│   ├── SRR975551_2.fastq.gz
│   ├── SRR975552_2.fastq.gz
│   └── (and other fastq.gz files)
│   └── hg38.rRNA.fasta
|   └── hg38.genome.fasta
|   └── hg38.annotation.gtf
|   └── (and other files)
└── data.txt 
```  
Copy your RNA-seq reads (\*.fastq.gz), rRNA sequences (\*.fa), reference genomes (\*.fa), related sp. reference genome (\*.fa), annotations (\*.gtf) and liftover files in data directory; create file data.txt in the same by using data_template.txt and add paths for raw fastq.gz, rRNA sequences, reference genome, rel sp. reference genome, annotations and liftover files in the same \
If you don't have reference genome, annotations, and rRNA sequence information; you can download the same with the script provided with the pipeline as follows
```
python check_ensembl.py org_name
eg. python find_species_in_ensembl.py Sorghum
> sbicolor
python ensembl.py org_name_in_ensembl
eg. python download_datasets_ensembl.py sbicolor
> Ensembl version 56 <- download the datasets
```
Similarly, if you don't have liftover files for conservation analysis then you can keep it blank as such or generate it through genome alignments of reference and query species genomes as follows
```
python Liftover.py <threads> <genome> <org_name> <genome_related_species> <rel_sp_name> <params_distance>
eg.
python Liftover.py 16 Sorghum_bicolor.dna.toplevel.fa Sbicolor Zea_mays.dna.toplevel.fa Zmays near
```

This will produce protein-coding, non-coding, mirRNA, and snoRNA bed files for slncky. 
7. Pipeline is ready for executaion \
Run following command and execute the steps for lncRNAs and NPCTs analysis 
```
bpipe run -n ${threads} ~/Path_to_LncConserve/Main.groovy data.txt
```

## Thanks for using LncConseve !!
