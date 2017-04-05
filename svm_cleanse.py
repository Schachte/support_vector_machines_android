'''
0: Eating
1: Walking
2: Running

[1] = Activity

[i % 3 == 2] = 0 x
[i % 3 == 0] = 1 y
[i % 3 == 1] = 2 z

'''

def read_file(file_name):
    return open(file_name, 'r')
    
def get_activity(type):
    type = type.lower().strip()
    if (type == 'eating'):
        return 0
    elif (type == 'walking'):
        return 1
    elif (type == 'running'):
        return 2
    else: 
        return 'null'

def main():
    data_file = read_file('android.txt')
    written_data = open('movement_output.txt', 'w')
    
    for index, line in enumerate(data_file):
        line = line.split(',')
        activity_type = get_activity(line[1])
        
        x_sum = 0
        y_sum = 0
        z_sum = 0
        
        x_count = 0
        y_count = 0
        z_count = 0
        
        for datapoint in range(2, len(line)):
            if (datapoint % 3 == 2):
                '''This is for x-value'''
                x_sum += float(line[datapoint])
                x_count+=1
                
            elif (datapoint % 3 == 0):
                '''This is for y-value'''
                y_sum += float(line[datapoint])
                y_count+=1
                
            elif (datapoint % 3 == 1):
                '''This is for z-value'''
                z_sum += float(line[datapoint])
                z_count+=1
                

        print("%d,%.10f#%.10f#%.10f"%(activity_type, x_sum, y_sum, z_sum))
        written_data.write("%d,%.10f#%.10f#%.10f\n"%(activity_type, x_sum, y_sum, z_sum))
                
        print('\n')
main()
