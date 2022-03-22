<template>
    <div style="text-align: center;">
        <el-row :gutter="10" style="margin-bottom: 60px">
            <el-col :span="8">
                <el-card style="color: #409EFF">
                    <div><i class="el-icon-user-solid"/> 用户总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        100
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card style="color: #F56C6C">
                    <div><i class="el-icon-money"/> 部门总量</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        ￥ 1000000
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card style="color: #67C23A">
                    <div><i class="el-icon-bank-card"/> 人均工资</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        ￥ 300000
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-row :span="24">
            <div id="main" style="height: 550px;"></div>
        </el-row>
    </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
    name: "Home",
    data() {
        return {}
    },
    mounted() {
        var chartDom = document.getElementById('main');
        var myChart = echarts.init(chartDom);
        var option;

        option = {
            tooltip: {
                trigger: 'item'
            },
            legend: {
                top: 'bottom'
            },
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            series: [
                {
                    name: 'Nightingale Chart',
                    type: 'pie',
                    radius: [50, 250],
                    center: ['50%', '50%'],
                    roseType: 'area',
                    itemStyle: {
                        borderRadius: 8
                    },
                    data: [
                        {value: 40, name: 'rose 1'},
                        {value: 38, name: 'rose 2'},
                        {value: 32, name: 'rose 3'},
                        {value: 30, name: 'rose 4'},
                        {value: 28, name: 'rose 5'},
                        {value: 26, name: 'rose 6'},
                        {value: 22, name: 'rose 7'},
                        {value: 18, name: 'rose 8'}
                    ]
                }
            ]
        };

        option && myChart.setOption(option);
    }
}
</script>

<style scoped>

</style>
